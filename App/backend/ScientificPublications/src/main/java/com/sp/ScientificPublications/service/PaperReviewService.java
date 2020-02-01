package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;

@Service
public class PaperReviewService {
	
	@Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;
    
    private static final String schemaPath = "src/main/resources/data/xsd_schema/paper-review.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/paper-review-fo.xsl";
    private static final String templatePath = "src/main/resources/templates/paper-review-template.xml";
    private static final String xsltFilePath = "src/main/resources/data/xslt/paper-review-xslt.xsl";

    private static final String collectionId = "/db/scientific-publication/paper-reviews";
    private static final String modelPackage = "com.sp.ScientificPublications.models.paper_review";

	public boolean validatePaperReviewXMLFile(MultipartFile file) {
    	return this.validatePaperReview(domParserSvc.readMultipartXMLFile(file));
    }
    
    public DocumentDTO uploadPaperReviewXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();
    	document.setDocumentContent(xmlContent);
    	document = this.storePaperReviewAsDocument(document);
    	this.generatePdf(document.getDocumentId());
    	return document;
    }
    
    public String generatePdf(String documentId) {
    	DocumentDTO retrievedDTO = this.retrievePaperReviewAsDocument(documentId);
    	retrievedDTO.setDocumentId("paper-review/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFilePath);
    }
    
    public String generateHtml(String documentId) {
    	DocumentDTO retrievedDTO = this.retrievePaperReviewAsDocument(documentId);
    	retrievedDTO.setDocumentId("scientific-paper/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generateHtmlFromXml(retrievedDTO, xsltFilePath);
    }
    
    public boolean validatePaperReview(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }
	
    public DocumentDTO retrievePaperReviewAsDocument(String documentId) {

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
    
    public DocumentDTO storePaperReviewAsDocument(DocumentDTO documentDTO) {

        if(!validatePaperReview(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The paper review is not in a valid format");
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
