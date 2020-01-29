package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.ConnectionProperties;
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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Repository
public class ExistJaxbRepository {

    @Autowired
    ConnectionProperties connProperties;

    @Autowired
    Database database;

    @Autowired
    ExistUtilityService existUtilSvc;


    public Object retrieveObject(String collectionId, String documentId, String modelPackage)
            throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;

        try {
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find document");
            }

            System.out.println("[INFO] Binding XML resource to an JAXB instance: ");
            JAXBContext context = JAXBContext.newInstance(modelPackage);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return unmarshaller.unmarshal(res.getContentAsDOM());

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

    }


    public Object storeObject(String collectionId, String documentId, String modelPackage,
                                        Object object) throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;
        OutputStream os = new ByteArrayOutputStream();

        try {

            col = existUtilSvc.getOrCreateCollection(collectionId);
            JAXBContext context = JAXBContext.newInstance(modelPackage);
            res = (XMLResource) col.getResource(documentId);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(object, os);

            res.setContent(os);
            System.out.println("[INFO] Storing the document: " + res.getId());

            col.storeResource(res);
            System.out.println("[INFO] Done.");

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return object;
    }

}
