package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.repository.exist.ExistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

@Service
public class DocumentReviewService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistRepository existRepo;
	
    private static final String schemaPath = "src/main/resources/data/xsd_schema/document-review.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/document-review-fo.xsl";
    private static final String collectionId = "/db/scientific-publication/document-reviews";

    public boolean validateDocumentReviewXMLFile(MultipartFile file) {
    	return this.validateDocumentReview(domParserSvc.readMultipartXMLFile(file));
    }

    public boolean validateDocumentReview(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String documentId) {
        return xmlTransformSvc.generatePdfFromXml(retrieveDocumentReview(documentId), xslFilePath);
    }


    public DocumentDTO retrieveDocumentReview(String fileId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existRepo.retrieveXmlFile(collectionId, fileId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover document review");
        }

        return document;

    }

    public DocumentDTO storeDocumentReview(DocumentDTO documentDTO) {

        if(!validateDocumentReview(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The document review is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existRepo.storeXmlFile(collectionId, documentDTO);
            documentDTO.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException ex) {
            ex.printStackTrace();
            throw new ApiBadRequestException("Failed to store xml file");
        }

        return documentDTO;

    }
	
}
