package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.repository.exist.ExistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

@Service
public class CoverLetterService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistRepository existRepo;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/cover-letter.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/cover-letter-fo.xsl";
    private static final String collectionId = "/db/scientific-publication/cover-letters";


    public boolean validateCoverLetter(String xmlDocumentPath) {
        return domParserSvc.validateXmlDocument(xmlDocumentPath, schemaPath);
    }

    public String generatePdf(String xmlDocumentPath) {
        return xmlTransformSvc.generatePdfFromXml(xmlDocumentPath, xslFilePath);
    }

    public DocumentDTO storeCoverLetter(DocumentDTO documentDTO) {

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
