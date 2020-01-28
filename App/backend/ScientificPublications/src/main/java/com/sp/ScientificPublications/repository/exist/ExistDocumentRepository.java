package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.ConnectionProperties;
import com.sp.ScientificPublications.models.cover_letter.CoverLetter;
import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import java.util.UUID;

@Repository
public class ExistRepository {

    @Autowired
    ConnectionProperties connProperties;

    @Autowired
    Database database;

    @Autowired
    ExistUtilityService existUtilSvc;


    public XMLResource storeXmlFile(String collectionId, DocumentDTO document) throws XMLDBException {

        Collection col = null;
        XMLResource res = null;

        try {
            System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = existUtilSvc.getOrCreateCollection(collectionId);

            // generating unique id for the document if it does not exist
            if(document.getDocumentId() == null || "".equals(document.getDocumentId())) {
                document.setDocumentId(UUID.randomUUID().toString());
            }

            System.out.println("[INFO] Inserting the document: " + document.getDocumentId());
            res = (XMLResource) col.createResource(document.getDocumentId(), XMLResource.RESOURCE_TYPE);

            res.setContent(document.getDocumentContent());
            System.out.println("[INFO] Storing the document: " + res.getId());

            col.storeResource(res);
            System.out.println("[INFO] Done.");

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return res;
    }


    public Object retrieveJaxbObject(String collectionId, String documentId, String modelPackage)
            throws XMLDBException, JAXBException {

        Collection col = null;
        XMLResource res = null;

        try {
            //System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            //System.out.println("[INFO] Retrieving the document: " + documentId);
            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find document");
            }

            System.out.println("[INFO] Binding XML resouce to an JAXB instance: ");
            JAXBContext context = JAXBContext.newInstance(modelPackage);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            CoverLetter coverLetter = (CoverLetter) unmarshaller.unmarshal(res.getContentAsDOM());
            System.out.println(coverLetter);


        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return null;
    }

    public XMLResource retrieveXmlFile(String collectionId, String documentId) throws XMLDBException {

        Collection col = null;
        XMLResource res = null;

        try {
            System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = existUtilSvc.getOrCreateCollection(collectionId);
            col.setProperty(OutputKeys.INDENT, "yes");

            System.out.println("[INFO] Retrieving the document: " + documentId);
            res = (XMLResource)col.getResource(documentId);

            if(res == null) {
                throw new ApiNotFoundException("Failed to find document");
            }

        } finally {
            existUtilSvc.cleanUp(col, res);
        }

        return res;
    }



}
