package com.sp.ScientificPublications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoverLetterService {

    @Autowired
    DomParserService domParserSvc;

    private final String schemaPath = "src/main/resources/data/schemas/cover-letter.xsd";


    public boolean validateCoverLetter(String xmlDocumentPath) {
        return domParserSvc.validateXmlDocument(xmlDocumentPath, schemaPath);
    }
}
