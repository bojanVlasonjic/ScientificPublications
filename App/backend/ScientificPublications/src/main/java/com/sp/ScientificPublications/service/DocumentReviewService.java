package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.document_review.DocumentReview;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;
import com.sp.ScientificPublications.utility.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentReviewService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;
	
    private static final String schemaPath = "src/main/resources/data/xsd_schema/document-review.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/document-review-fo.xsl";
    private static final String templatePath = "src/main/resources/templates/document-review-template.xml";

    private static final String collectionId = "/db/scientific-publication/document-reviews";
    private static final String modelPackage = "com.sp.ScientificPublications.models.document_review";


    public DocumentDTO getTemplate() {

        DocumentDTO templateDTO = new DocumentDTO();
        try {
            templateDTO.setDocumentContent(FileUtil.readFile(templatePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiBadRequestException("Failed to generate document review template");
        }

        return templateDTO;
    }

    public boolean validateDocumentReview(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }
    
    // ================= File manipulation
    
    public boolean validateDocumentReviewXMLFile(MultipartFile file) {
    	return this.validateDocumentReview(domParserSvc.readMultipartXMLFile(file));
    }
    
    public DocumentDTO uploadDocumentReviewXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();
    	document.setDocumentContent(xmlContent);
    	document = this.storeDocumentReviewAsDocument(document);
    	this.generatePdf(document.getDocumentId());
    	return document;
    }
    
    // =================

    public String generatePdf(String documentId) {
    	DocumentDTO retrievedDTO = this.retrieveDocumentReviewAsDocument(documentId);
    	retrievedDTO.setDocumentId("document-review/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFilePath);
    }


    public DocumentReview retrieveDocumentReviewAsObject(String documentId) {

        try {
            return (DocumentReview) existJaxbRepo.retrieveObject(collectionId, documentId, modelPackage);
        } catch (XMLDBException | JAXBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve document review");
        }

    }

    public DocumentDTO retrieveDocumentReviewAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover document review");
        }

        return document;

    }


    // TODO: implement an update method, change method name
    public DocumentReview storeDocumentReviewAsObject(String documentId) {

        DocumentReview docReview = retrieveDocumentReviewAsObject(documentId);

        //TODO: update data
        docReview.setTitle("Say no to lombok");

        try {
            return (DocumentReview) existJaxbRepo.storeObject(collectionId, documentId, modelPackage, docReview);
        } catch (XMLDBException | JAXBException ex) {
            throw new ApiBadRequestException("Failed to store document review");
        }

    }

    public DocumentDTO storeDocumentReviewAsDocument(DocumentDTO documentDTO) {

        if(!validateDocumentReview(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The document review is not in a valid format");
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
