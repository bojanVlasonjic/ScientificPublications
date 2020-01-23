package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScientificPublicationService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/scientific-paper.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/ADD_FILE_NAME_HERE";
    private static final String collectionId = "/db/scientific-publication/scientific-publications";


    public boolean validateScientificPublication(String xmlDocumentPath) {
        return domParserSvc.validateXmlDocument(xmlDocumentPath, schemaPath);
    }

    public String generatePdf(String xmlDocumentPath) {
        return xmlTransformSvc.generatePdfFromXml(xmlDocumentPath, xslFilePath);
    }
}
