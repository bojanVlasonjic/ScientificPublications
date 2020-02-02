package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.ExistConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.xml.transform.OutputKeys;
import java.util.UUID;

@Repository
public class ExistDocumentRepository {

    @Autowired
    ExistConnectionProperties connProperties;

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
