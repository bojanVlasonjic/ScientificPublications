package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScientificPublicationService {

    @Autowired
    DomParserService domParserSvc;

    private final String schemaPath = "src/main/resources/data/schemas/scientific-paper.xsd";


    public boolean validateScientificPublication(String xmlDocumentPath) {
        return domParserSvc.validateXmlDocument(xmlDocumentPath, schemaPath);
    }
}
