package com.sp.ScientificPublications.repository.rdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.xml.transform.TransformerException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;

import com.sp.ScientificPublications.models.FusekiConnectionProperties;

@Repository
public class FusekiDocumentRepository {
	
	@Autowired
	FusekiConnectionProperties conn;
	
	private static int connected = 0;
	private static final String SPARQL_NAMED_GRAPH_URI = "/scientific-paper/sparql/metadata";
	
	@PostConstruct
	private void init() {
		System.out.println("[INFO] Creating RDF dataset");
		try {
			AuthenticationUtilities.createDataset(conn);
			connected = 1;
		}
		catch(Exception e) {
			System.out.println("[INFO] Couldn't reach FUSEKI");
		}
		
	}
	
	//TODO implement reading
	public void run(FusekiConnectionProperties conn) throws IOException {

		// SPARQL file which is to be queried
		String sparqlFilePath = "data/sparql/query6.rq";

		// Querying the named graph with a referenced SPARQL query
		System.out.println("[INFO] Loading SPARQL query from file \"" + sparqlFilePath + "\"");
		String sparqlQuery = String.format(FileUtil.readFile(sparqlFilePath, StandardCharsets.UTF_8), 
				conn.dataEndpoint + SPARQL_NAMED_GRAPH_URI);
		
		System.out.println(sparqlQuery);
		
		// Create a QueryExecution that will access a SPARQL service over HTTP
		QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);

		// Query the SPARQL endpoint, iterate over the result set...
		System.out.println("[INFO] Showing the results for SPARQL query using the result handler.\n");
		ResultSet results = query.execSelect();

		String varName;
		RDFNode varValue;

		while (results.hasNext()) {

			// A single answer from a SELECT query
			QuerySolution querySolution = results.next();
			Iterator<String> variableBindings = querySolution.varNames();

			// Retrieve variable bindings
			while (variableBindings.hasNext()) {

				varName = variableBindings.next();
				varValue = querySolution.get(varName);

				System.out.println(varName + ": " + varValue);
			}
			System.out.println();
		}

		// Issuing the same query once again...

		// Create a QueryExecution that will access a SPARQL service over HTTP
		query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);

		// Query the collection, dump output response as XML
		System.out.println("[INFO] Showing the results for SPARQL query in native SPARQL XML format.\n");
		results = query.execSelect();

		//ResultSetFormatter.outputAsXML(System.out, results);
		ResultSetFormatter.out(System.out, results);

		query.close();

		System.out.println("[INFO] End.");

	}
	
	public void extractMetadata(String xmlContent) throws IOException, SAXException, TransformerException {
		
		if (connected == 0) {
			AuthenticationUtilities.createDataset(conn);
			connected = 1;
		}
	
		System.out.println("[INFO] " + FusekiDocumentRepository.class.getSimpleName());
		
		
		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = new MetadataExtractor();
		
		InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		System.out.println("[INFO] Extracting metadata from RDFa attributes...");
		metadataExtractor.extractMetadata(
				inputStream, 
				outputStream);
				
		
		// Loading a default model with extracted metadata
		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(outputStream.toByteArray()), null);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		model.write(out, SparqlUtil.NTRIPLES);
		
		System.out.println("[INFO] Extracted metadata as RDF/XML...");
		model.write(System.out, SparqlUtil.RDF_XML);

		
		// Writing the named graph
		System.out.println("[INFO] Populating named graph \"" + SPARQL_NAMED_GRAPH_URI + "\" with extracted metadata.");
		String sparqlUpdate = SparqlUtil.insertData(conn.dataEndpoint + SPARQL_NAMED_GRAPH_URI, new String(out.toByteArray()));
		System.out.println(sparqlUpdate);
		
		// UpdateRequest represents a unit of execution
		UpdateRequest update = UpdateFactory.create(sparqlUpdate);

		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, conn.updateEndpoint);
		processor.execute();
		
		
		
		// Read the triples from the named graph
		System.out.println();
		System.out.println("[INFO] Retrieving triples from RDF store.");
		System.out.println("[INFO] Using \"" + SPARQL_NAMED_GRAPH_URI + "\" named graph.");

		System.out.println("[INFO] Selecting the triples from the named graph \"" + SPARQL_NAMED_GRAPH_URI + "\".");
		String sparqlQuery = SparqlUtil.selectData(conn.dataEndpoint + SPARQL_NAMED_GRAPH_URI, "?s ?p ?o");
		
		// Create a QueryExecution that will access a SPARQL service over HTTP
		QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
		
		
		// Query the collection, dump output response as XML
		ResultSet results = query.execSelect();
		
		ResultSetFormatter.out(System.out, results);
		
		query.close() ;
		
		System.out.println("[INFO] End.");
	}
}
