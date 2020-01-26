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

    public boolean validateCoverLetterXMLFile(MultipartFile file) {
    	return this.validateCoverLetter(domParserSvc.readMultipartXMLFile(file));
    }

    public boolean validateCoverLetter(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String documentId) {
        return xmlTransformSvc.generatePdfFromXml(retrieveCoverLetter(documentId), xslFilePath);
    }

    public DocumentDTO retrieveCoverLetter(String fileId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existRepo.retrieveXmlFile(collectionId, fileId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover letter");
        }

        return document;
    }

    public DocumentDTO storeCoverLetter(DocumentDTO documentDTO) {

        if(!validateCoverLetter(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The cover letter is not in a valid format");
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
