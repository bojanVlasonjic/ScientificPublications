package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.cover_letter.CoverLetter;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;
import com.sp.ScientificPublications.utility.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;


@Service
public class CoverLetterService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;
    
    @Autowired
    UtilityService utilityService;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/cover-letter.xsd";
    private static final String xslFoFilePath = "src/main/resources/data/xsl_fo/cover-letter-fo.xsl";
    private static final String xsltFilePath = "src/main/resources/data/xslt/cover-letter-xslt.xsl";
    private static final String templatePath = "src/main/resources/templates/cover-letter-template.xml";
    
    private static final String PDF_DIRECTORY_PATH = "src/main/resources/output/pdf/cover-letter/";
    private static final String HTML_DIRECTORY_PATH = "src/main/resources/output/html/cover-letter/";

    private static final String collectionId = "/db/scientific-publication/cover-letters";
    private static final String modelPackage = "com.sp.ScientificPublications.models.cover_letter";

    // ===================== DOWNLOAD =====================
    public ResponseEntity<InputStreamResource> downloadPDF(String xmlId) throws IOException {
        String path = PDF_DIRECTORY_PATH + xmlId + ".pdf";
        File file = new File(path);
        if (!file.exists()) throw new ApiBadRequestException("No such file");
        
        HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("attachment", file.getName());
        
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(inputStream);
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    public ResponseEntity<InputStreamResource> downloadHTML(String xmlId) throws IOException {
        String path = HTML_DIRECTORY_PATH + xmlId + ".html";
        File file = new File(path);
        if (!file.exists()) throw new ApiBadRequestException("No such file");
        
        HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/html"));
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("attachment", file.getName());
        
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(inputStream);
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    public ResponseEntity<InputStreamResource> downloadXML(String xmlId) throws XMLDBException {
    	XMLResource res = existDocumentRepo.retrieveXmlFile(collectionId, xmlId);
    	
    	InputStreamResource resource = new InputStreamResource(
    			new ByteArrayInputStream(res.getContent().toString().getBytes()));
    	
    	HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml"));
        headers.setContentDispositionFormData("attachment", xmlId + ".xml");
    	
    	return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
 	// =========================================================================
    
    // ============================== VIEW ==============================
    public ResponseEntity<byte[]> viewCoverLetter(String id) throws URISyntaxException, IOException {
    	
    	String filename = PDF_DIRECTORY_PATH + id + ".pdf";
    	File pdf = new File(filename);
    	if (!pdf.exists()) throw new ApiBadRequestException("No such file");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.parseMediaType("application/pdf"));
    	headers.add("content-disposition", "inline;filename=" + filename);
    	headers.setCacheControl("must-revalidate, post-check=0 pre-check=0");
    	
    	byte[] file = xmlTransformSvc.loadFile(filename);
    	
    	return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
    }
    // ============================================================


    public DocumentDTO getTemplate() {

        DocumentDTO templateDTO = new DocumentDTO();
        try {
            templateDTO.setDocumentContent(FileUtil.readFile(templatePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiBadRequestException("Failed to generate cover letter template");
        }

        return templateDTO;
    }

    // ================= File manipulation
    
    public boolean validateCoverLetterXMLFile(MultipartFile file) {
    	return this.validateCoverLetter(domParserSvc.readMultipartXMLFile(file));
    }
    
    public DocumentDTO uploadCoverLetterXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();
    	document.setDocumentContent(xmlContent);
    	document = this.storeCoverLetterAsDocument(document);
    	this.generatePdf(document.getDocumentId());
    	this.generateHtml(document.getDocumentId());
    	return document;
    }
    // =================

    public boolean validateCoverLetter(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }


    public String generatePdf(String documentId) {
    	DocumentDTO retrievedDTO = this.retrieveCoverLetterAsDocument(documentId);
    	retrievedDTO.setDocumentId("cover-letter/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFoFilePath);
    }

    public String generateHtml(String documentId) {
    	DocumentDTO retrievedDTO = this.retrieveCoverLetterAsDocument(documentId);
    	retrievedDTO.setDocumentId("cover-letter/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generateHtmlFromXml(retrievedDTO, xsltFilePath);
    }


    public CoverLetter retrieveCoverLetterAsObject(String documentId) {

        try {
            return (CoverLetter) existJaxbRepo.retrieveObject(collectionId, documentId, modelPackage);
        } catch (XMLDBException | JAXBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve cover letter");
        }

    }

    public DocumentDTO retrieveCoverLetterAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover letter");
        }

        return document;
    }

    // TODO: implement an update method, change method name
    public CoverLetter storeCoverLetterAsObject(String documentId) {

        CoverLetter coverLetter = retrieveCoverLetterAsObject(documentId);

        //TODO: update data
        coverLetter.getBody().setSalutation("Dear Mister Misses Something");

        try {
            return (CoverLetter) existJaxbRepo.storeObject(collectionId, documentId, modelPackage, coverLetter);
        } catch (XMLDBException | JAXBException ex) {
            throw new ApiBadRequestException("Failed to store cover letter");
        }

    }


    public DocumentDTO storeCoverLetterAsDocument(DocumentDTO documentDTO) {

        if(!validateCoverLetter(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The cover letter is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existDocumentRepo.storeXmlFile(collectionId, documentDTO);
            documentDTO.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException ex) {
            ex.printStackTrace();
            throw new ApiBadRequestException("Failed to store xml file");
        }

        return documentDTO;
    }

}
