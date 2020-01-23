package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.repository.exist.ExistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

@Service
public class ScientificPublicationService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistRepository existRepo;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/scientific-paper.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/ADD_FILE_NAME_HERE";
    private static final String collectionId = "/db/scientific-publication/scientific-publications";


    public boolean validateScientificPublication(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String xmlDocumentPath) {
        return xmlTransformSvc.generatePdfFromXml(xmlDocumentPath, xslFilePath);
    }

    public DocumentDTO retrieveScientificPublication(String fileId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existRepo.retrieveXmlFile(collectionId, fileId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the scientific publication");
        }

        return document;
    }

    public DocumentDTO storeScientificPublication(DocumentDTO document) {

        if(!validateScientificPublication(document.getDocumentContent())) {
            throw new ApiBadRequestException("The scientific publication is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existRepo.storeXmlFile(collectionId, document);
            document.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to save scientific publication");
        }

        return document;
    }


}
