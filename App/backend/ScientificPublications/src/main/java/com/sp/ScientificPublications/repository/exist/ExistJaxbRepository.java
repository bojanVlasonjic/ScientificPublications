package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.config.ConnectionProperties;
import com.sp.ScientificPublications.models.cover_letter.CoverLetter;
import com.sp.ScientificPublications.models.document_review.DocumentReview;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;

@Repository
public class ExistJaxbRepository {

    @Autowired
    ConnectionProperties connProperties;

    @Autowired
    Database database;

    @Autowired
    ExistUtilityService existUtilSvc;


    /***********
     * RETRIEVAL *
     * ********/

    public CoverLetter retrieveCoverLetter(String collectionId, String documentId, String modelPackage)
            throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;
        CoverLetter coverLetter;

        try {
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find cover letter");
            }

            System.out.println("[INFO] Binding XML resource to an JAXB instance: ");
            JAXBContext context = JAXBContext.newInstance(modelPackage);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            coverLetter = (CoverLetter) unmarshaller.unmarshal(res.getContentAsDOM());

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return coverLetter;
    }


    public ScientificPaper retrieveScientificPaper(String collectionId, String documentId, String modelPackage)
            throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;
        ScientificPaper scientificPaper;

        try {
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find scientific paper");
            }

            System.out.println("[INFO] Binding XML resource to an JAXB instance: ");
            JAXBContext context = JAXBContext.newInstance(modelPackage);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            scientificPaper = (ScientificPaper) unmarshaller.unmarshal(res.getContentAsDOM());

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return scientificPaper;
    }


    public DocumentReview retrieveDocumentReview(String collectionId, String documentId, String modelPackage)
            throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;
        DocumentReview documentReview;

        try {
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find document review");
            }

            System.out.println("[INFO] Binding XML resource to an JAXB instance: ");
            JAXBContext context = JAXBContext.newInstance(modelPackage);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            documentReview = (DocumentReview) unmarshaller.unmarshal(res.getContentAsDOM());

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return documentReview;
    }


    /***********
     * STORING *
     * ********/

}
