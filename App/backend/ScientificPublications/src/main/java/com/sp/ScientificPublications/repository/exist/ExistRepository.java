package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.models.ConnectionProperties;
import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import java.util.UUID;

@Repository
public class ExistRepository {

    @Autowired
    ConnectionProperties connProperties;

    @Autowired
    Database database;


    public XMLResource storeXmlFile(String collectionId, DocumentDTO document) throws XMLDBException {

        Collection col = null;
        XMLResource res = null;

        try {
            System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = getOrCreateCollection(collectionId);

            // generating unique id for the document
            document.setDocumentId(UUID.randomUUID().toString());

            System.out.println("[INFO] Inserting the document: " + document.getDocumentId());
            res = (XMLResource) col.createResource(document.getDocumentId(), XMLResource.RESOURCE_TYPE);

            res.setContent(document.getDocumentContent());
            System.out.println("[INFO] Storing the document: " + res.getId());

            col.storeResource(res);
            System.out.println("[INFO] Done.");

        } finally {
            //clean up
            if(res != null) {
                try {
                    ((EXistResource)res).freeResources();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }

            if(col != null) {
                try {
                    col.close();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }

        return res;
    }


    public XMLResource retrieveXmlFile(String collectionId, String fileId) {
        //TODO: implement

        return null;
    }


    private Collection getOrCreateCollection(String collectionUri) throws XMLDBException {
        return getOrCreateCollection(collectionUri, 0);
    }

    private Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {

        Collection col = DatabaseManager.getCollection(connProperties.getUri() + collectionUri,
                connProperties.getUser(), connProperties.getPassword());

        if(col == null) {

            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }

            String[] pathSegments = collectionUri.split("/");

            if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();

                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/");
                    path.append(pathSegments[i]);
                }

                Collection startCol = DatabaseManager.getCollection(connProperties.getUri() + path,
                        connProperties.getUser(), connProperties.getPassword());

                if (startCol == null) {

                    // child collection does not exist

                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parentCol = DatabaseManager.getCollection(connProperties.getUri() + parentPath,
                            connProperties.getUser(), connProperties.getPassword());

                    CollectionManagementService mgt = (CollectionManagementService) parentCol.getService("CollectionManagementService", "1.0");

                    System.out.println("[INFO] Creating the collection: " + pathSegments[pathSegmentOffset]);
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);

                    col.close();
                    parentCol.close();

                } else {
                    startCol.close();
                }
            }
            return getOrCreateCollection(collectionUri, ++pathSegmentOffset);

        } else {
            return col;
        }

    }
}
