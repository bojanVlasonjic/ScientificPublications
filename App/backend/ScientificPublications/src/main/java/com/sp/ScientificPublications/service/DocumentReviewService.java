package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentReviewService {
	
	@Autowired
    DomParserService domParserSvc;
	
	@Autowired
    XmlTransformerService xmlTransformSvc;
	
	private static final String schemaPath = "src/main/resources/data/xsd_schema/document-review.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/document-review-fo.xsl";


    public boolean validateDocumentReview(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String xmlDocumentPath) {
        return xmlTransformSvc.generatePdfFromXml(xmlDocumentPath, xslFilePath);
    }
	
}
