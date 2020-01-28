package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.cover_letter.CoverLetter;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBException;


@Service
public class CoverLetterService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/cover-letter.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/cover-letter-fo.xsl";
    private static final String collectionId = "/db/scientific-publication/cover-letters";
    private static final String modelPackage = "com.sp.ScientificPublications.models.cover_letter";


    public boolean validateCoverLetterXMLFile(MultipartFile file) {
    	return this.validateCoverLetter(domParserSvc.readMultipartXMLFile(file));
    }

    public boolean validateCoverLetter(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String documentId) {
        return xmlTransformSvc.generatePdfFromXml(retrieveCoverLetterAsDocument(documentId), xslFilePath);
    }


    public CoverLetter retrieveCoverLetterAsObject(String documentId) {

        CoverLetter coverLetter = null;

        try {
            coverLetter = existJaxbRepo.retrieveCoverLetter(collectionId, documentId, modelPackage);
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve cover letter");
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return coverLetter;

    }

    public DocumentDTO retrieveCoverLetterAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover letter");
        }

        return document;
    }


    public DocumentDTO storeCoverLetterAsDocument(DocumentDTO documentDTO) {

        if(!validateCoverLetter(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The cover letter is not in a valid format");
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
