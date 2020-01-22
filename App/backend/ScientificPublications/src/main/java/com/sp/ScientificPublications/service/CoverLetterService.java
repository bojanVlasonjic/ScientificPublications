package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoverLetterService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/cover-letter.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/cover-letter-fo.xsl";


    public boolean validateCoverLetter(String xmlDocumentPath) {
        return domParserSvc.validateXmlDocument(xmlDocumentPath, schemaPath);
    }

    public String generatePdf(String xmlDocumentPath) {
        return xmlTransformSvc.generatePdfFromXml(xmlDocumentPath, xslFilePath);
    }
}
