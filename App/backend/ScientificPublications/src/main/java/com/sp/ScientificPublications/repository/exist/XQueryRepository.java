package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.exception.ApiInternalServerException;
import com.sp.ScientificPublications.models.ExistConnectionProperties;
import com.sp.ScientificPublications.utility.FileUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

@Repository
public class XQueryRepository {
	
	@Autowired
	ExistConnectionProperties existConnectionProperties;

	public ResourceSet find(String collectionId, String xqueryExpression) {
        String uri = existConnectionProperties.getUri() + collectionId;

        try {
            Collection spCollection = DatabaseManager.getCollection(uri, "admin", "");
            XQueryService xQueryService = (XQueryService) spCollection.getService("XQueryService", "1.0");
            CompiledExpression compiledXquery = xQueryService.compile(xqueryExpression);
            return xQueryService.execute(compiledXquery);
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiInternalServerException("Error while comunicationg with xml database.");
        }
    }
	
	public List<String> getAllDocumentIDS(String collectionId) {
		List<String> documentIDS = new ArrayList<String>();
		String queryFilePath = "./src/main/resources/data/xquery/get-all-document-uris.xqy";
    	try {
    		String query = FileUtil.readFile(queryFilePath, StandardCharsets.UTF_8);
    		ResourceSet set = this.find(collectionId, query);
    		ResourceIterator iter = set.getIterator();
    		while(iter.hasMoreResources()) {
    			Resource res = iter.nextResource();
    			documentIDS.add(res.getContent().toString());
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return documentIDS;
	}
	
	public List<String> getAllReferencesFromScientificPaper(String collectionId, String query, String id) {
		query = String.format(query, id);
		List<String> references = new ArrayList<String>();
		try {
			ResourceSet set = this.find(collectionId, query);
			ResourceIterator iter = set.getIterator();
			while(iter.hasMoreResources()) {
				Resource res = iter.nextResource();
				references.add(res.getContent().toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return references;
	}
}
