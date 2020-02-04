package com.sp.ScientificPublications.repository.exist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import com.sp.ScientificPublications.models.ExistConnectionProperties;

@Repository
public class XQueryRepository {
	
	@Autowired
	ExistConnectionProperties conn;
	
	private static final String TARGET_NAMESPACE = "http://www.ftn.uns.ac.rs/scientific-publications";
	
	public void getData(String collectionId, String id, String xqueryFilePath) throws Exception {
    	
		String xqueryExpression = null; 

        System.out.println("\t- collection ID: " + collectionId);
        System.out.println("\t- xQuery file path: " + xqueryFilePath);
        
    	// initialize database driver
    	System.out.println("[INFO] Loading driver class: " + conn.getDriver());
        Class<?> cl = Class.forName(conn.getDriver());
        
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        
        DatabaseManager.registerDatabase(database);
        
        Collection col = null;
        
        try { 

        	// get the collection
        	System.out.println("[INFO] Retrieving the collection: " + collectionId);
            col = DatabaseManager.getCollection(conn.getUri() + collectionId);
        	
            // get an instance of xquery service
            XQueryService xqueryService = (XQueryService) col.getService("XQueryService", "1.0");
            xqueryService.setProperty("indent", "yes");
            
            // make the service aware of namespaces
            xqueryService.setNamespace("sp", TARGET_NAMESPACE);

            // read xquery 
            System.out.println("[INFO] Invoking XQuery service for: " + xqueryFilePath);
            xqueryExpression = readFile(xqueryFilePath, StandardCharsets.UTF_8);
            xqueryExpression = String.format(xqueryExpression, id);
            System.out.println(xqueryExpression);
            
            // compile and execute the expression
            CompiledExpression compiledXquery = xqueryService.compile(xqueryExpression);
            ResourceSet result = xqueryService.execute(compiledXquery);
            
            // handle the results
            System.out.println("[INFO] Handling the results... ");
            
            ResourceIterator i = result.getIterator();
            Resource res = null;
            
            while(i.hasMoreResources()) {
            
            	try {
                    res = i.nextResource();
                    System.out.println(res.getContent());
                    
                } finally {
                    
                	// don't forget to cleanup resources
                    try { 
                    	((EXistResource)res).freeResources(); 
                    } catch (XMLDBException xe) {
                    	xe.printStackTrace();
                    }
                }
            }
            
        } finally {
        	
            // don't forget to cleanup
            if(col != null) {
                try { 
                	col.close();
                } catch (XMLDBException xe) {
                	xe.printStackTrace();
                }
            }
        }
    }
	
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
