package com.sp.ScientificPublications.repository.exist;

import com.sp.ScientificPublications.models.ExistConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XQueryService;

@Repository
public class XQueryRepository {
	
	@Autowired
	ExistConnectionProperties existConnectionProperties;

	public ResourceSet find(String collectionId, String xqueryExpression) throws Exception {
        String uri = existConnectionProperties.getUri() + collectionId;

        Collection spCollection = DatabaseManager.getCollection(uri, "admin", "");
        XQueryService xQueryService = (XQueryService) spCollection.getService("XQueryService", "1.0");
        CompiledExpression compiledXquery = xQueryService.compile(xqueryExpression);
        return xQueryService.execute(compiledXquery);
    }


}
