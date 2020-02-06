package com.sp.ScientificPublications.repository.rdf;

import com.sp.ScientificPublications.models.FusekiConnectionProperties;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Repository
public class RdfRepository {

    @Autowired
    private MetadataExtractor metadataExtractor;

    @Autowired
    private FusekiConnectionProperties fusekiConnectionProperties;

    private static final String SPARQL_NAMED_GRAPH_URI = "/metadata";

    public void saveMetadataFromXml(String xmlContent) {
        try {
            // create streams for input and outputs
            InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream nTriplesOutputStream = new ByteArrayOutputStream();

            // extract metadata in rdf/xml format
            metadataExtractor.extractMetadata(inputStream, outputStream);
            String xmlRdfContent = new String(outputStream.toByteArray());

            if (validateMetadata(xmlRdfContent)) {
                // convert metadata from rdf/xml to ntriples
                Model model = ModelFactory.createDefaultModel();

                model.read(new ByteArrayInputStream(outputStream.toByteArray()), null);
                model.createResource(SparqlUtil.SUBJECT_URI + "/author/john")
                        .addProperty(new PropertyImpl(SparqlUtil.PROPERTY_URI + "/parentTo"), "jane");

                model.write(nTriplesOutputStream, SparqlUtil.NTRIPLES);

                // create update expression
                String sparqlUpdate = SparqlUtil.insertData(
                        fusekiConnectionProperties.dataEndpoint + SPARQL_NAMED_GRAPH_URI,
                        new String(nTriplesOutputStream.toByteArray()));

                // save to db
                UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
                UpdateExecutionFactory.createRemote(updateRequest, fusekiConnectionProperties.updateEndpoint).execute();

                // ResultSet resultSet = getTripletsFromRdfDb("?s ?p ?o");
                // QuerySolution querySolution = resultSet.nextSolution();
                // System.out.println(querySolution.getResource("p"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveModelToDb(Model model) {
        ByteArrayOutputStream nTriplesOutputStream = new ByteArrayOutputStream();
        model.write(nTriplesOutputStream, SparqlUtil.NTRIPLES);
        String sparqlUpdate = SparqlUtil.insertData(
                fusekiConnectionProperties.dataEndpoint + SPARQL_NAMED_GRAPH_URI,
                new String(nTriplesOutputStream.toByteArray()));
        UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
        UpdateExecutionFactory.createRemote(updateRequest, fusekiConnectionProperties.updateEndpoint).execute();
    }

    // QUERY CONDITION FORMAT: ?s ?p ?o
    public ResultSet getTripletsFromRdfDb(String queryCondition) {
        String sparqlQuery = SparqlUtil.selectData(fusekiConnectionProperties.dataEndpoint + SPARQL_NAMED_GRAPH_URI, queryCondition);
        QueryExecution query = QueryExecutionFactory.sparqlService(fusekiConnectionProperties.queryEndpoint, sparqlQuery);
        ResultSet resultSet = query.execSelect();
        return resultSet;
    }

    private boolean validateMetadata(String rdfXmlContent) {
        if (rdfXmlContent.contains("<!--Could not produce the triple for:")) {
            System.out.println("VALIDATING RDF/XML FAILED.");
            return false;
        } else if (!rdfXmlContent.contains("rdf:Description")) {
            System.out.println("RDF/XML DONT HAVE rdf:Description.");
            return false;
        }
        return true;
    }

}
