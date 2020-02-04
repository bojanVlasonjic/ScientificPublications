package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.SearchByAuthorsResponseDTO;
import com.sp.ScientificPublications.dto.SendEmailDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;
import com.sp.ScientificPublications.repository.exist.XQueryRepository;
import com.sp.ScientificPublications.repository.rdf.FusekiDocumentRepository;

import com.sp.ScientificPublications.utility.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

@Service
public class ScientificPaperService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;
    
    @Autowired
    FusekiDocumentRepository fusekiDocumentRepository;
    
    @Autowired
    EmailSenderService emailSenderService;
    
    @Autowired
    XQueryRepository queryRepository;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/scientific-paper.xsd";
    private static final String xslFoFilePath = "src/main/resources/data/xsl_fo/scientific-paper-fo.xsl";
    private static final String xsltFilePath = "src/main/resources/data/xslt/scientific-paper-xslt.xsl";
    private static final String templatePath = "src/main/resources/templates/scientific-paper-template.xml";
    
    private static final String PDF_DIRECTORY_PATH = "src/main/resources/output/pdf/scientific-paper/";
    private static final String HTML_DIRECTORY_PATH = "src/main/resources/output/html/scientific-paper/";

    private static final String collectionId = "/db/scientific-publication/scientific-papers";
    private static final String modelPackage = "com.sp.ScientificPublications.models.scientific_paper";
    private static final String SPARQL_NAMED_GRAPH_URI = "/scientific-paper/sparql/metadata";


    public DocumentDTO getTemplate() {

        DocumentDTO templateDTO = new DocumentDTO();
        try {
            templateDTO.setDocumentContent(FileUtil.readFile(templatePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiBadRequestException("Failed to generate scientific paper template");
        }

        return templateDTO;
    }
    
    public boolean sendEmail(SendEmailDTO dto) {
    	
    	String pdfPath = PDF_DIRECTORY_PATH + dto.getPdfGUID() + ".pdf";
    	String htmlPath = HTML_DIRECTORY_PATH + dto.getHtmlGUID() + ".html";
    	
    	byte[] pdfBytes = null;
    	byte[] htmlBytes = null;
    	
    	try {
			pdfBytes = xmlTransformSvc.loadFile(pdfPath);
			htmlBytes = xmlTransformSvc.loadFile(htmlPath);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
    	
    	
    	Map<String, ByteArrayResource> attachmentNameAndByteResource = new HashMap<String, ByteArrayResource>();
    	attachmentNameAndByteResource.put("ScientificPaper.pdf", new ByteArrayResource(pdfBytes));
    	attachmentNameAndByteResource.put("ScientificPaper.html", new ByteArrayResource(htmlBytes));
    	
    	emailSenderService.sendMessageWithAttachments(
    			dto.getTo(), 
    			dto.getSubject(),
    			dto.getMessage(), 
    			attachmentNameAndByteResource);
    	return true;
    }

    // ================= File manipulation
    
    public boolean validateScientificPaperXMLFile(MultipartFile file) {
    	return this.validateScientificPaper(domParserSvc.readMultipartXMLFile(file));
    }
    
    public DocumentDTO uploadScientificPaperXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();
    	document.setDocumentContent(xmlContent);
    	document = this.storeScientificPaperAsDocument(document);
    	//this.generatePdf(document.getDocumentId());
    	//this.generateHtml(document.getDocumentId());
    	String queryFilePath = "./src/main/resources/data/xquery/get-scientific-paper-references.xqy";
    	try {
			this.queryRepository.getData(collectionId, document.getDocumentId(), queryFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return document;
    }
    
    // =================

    public boolean validateScientificPaper(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String documentId) {
    	DocumentDTO retrievedDTO = this.retrieveScientificPaperAsDocument(documentId);
    	retrievedDTO.setDocumentId("scientific-paper/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFoFilePath);
    }

    public String generateHtml(String documentId) {
    	DocumentDTO retrievedDTO = this.retrieveScientificPaperAsDocument(documentId);
    	retrievedDTO.setDocumentId("scientific-paper/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generateHtmlFromXml(retrievedDTO, xsltFilePath);
    }
    
    public SearchByAuthorsResponseDTO searchMetadataByAuthor(String author) throws IOException {
    	return fusekiDocumentRepository.searchMetadataByAuthor(author);
    }
    
    //fileName without extension
    public void extractMetaData(String xmlContent, String fileName) throws IOException, SAXException, TransformerException {
    	fusekiDocumentRepository.extractMetadata(xmlContent, fileName);
    }


    public ScientificPaper retrieveScientificPaperAsObject(String documentId) {

        try {
            return (ScientificPaper) existJaxbRepo.retrieveObject(collectionId, documentId, modelPackage);
        } catch (XMLDBException | JAXBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve scientific paper");
        }

    }


    public DocumentDTO retrieveScientificPaperAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the scientific publication");
        }

        return document;
    }


    // TODO: implement an update method, change method name
    public ScientificPaper storeScientificPaperAsObject(String documentId) {

        ScientificPaper scPaper = retrieveScientificPaperAsObject(documentId);

        //TODO: update data
        scPaper.getHeader().setTitle("Setter in a getter, woohoo!");

        try {
            return (ScientificPaper) existJaxbRepo.storeObject(collectionId, documentId, modelPackage, scPaper);
        } catch (XMLDBException | JAXBException ex) {
            throw new ApiBadRequestException("Failed to store scientific paper");
        }

    }


    public DocumentDTO storeScientificPaperAsDocument(DocumentDTO document) {

        if(!validateScientificPaper(document.getDocumentContent())) {
            throw new ApiBadRequestException("The scientific publication is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existDocumentRepo.storeXmlFile(collectionId, document);
            document.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to save scientific publication");
        }

        return document;
    }


}
