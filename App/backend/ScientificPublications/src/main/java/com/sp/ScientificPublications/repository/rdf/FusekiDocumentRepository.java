package com.sp.ScientificPublications.repository.rdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sp.ScientificPublications.dto.SearchByAuthorsResponseDTO;
import com.sp.ScientificPublications.models.FusekiConnectionProperties;
import com.sp.ScientificPublications.utility.FileUtil;
import org.apache.jena.query.*;
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

import javax.annotation.PostConstruct;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Repository
public class FusekiDocumentRepository {
	
	@Autowired
	FusekiConnectionProperties conn;
	
	private static int connected = 0;
	private static final String SPARQL_NAMED_GRAPH_URI = "/scientific-paper/sparql/metadata";
	private static final String SEARCH_BY_AUTHORS = "src/main/resources/data/rdf/sparql/search-by-authors.rq";
	private static final String METADATA_RDF_FILES_PATH = "./src/main/resources/output/metadata/rdf";
	private static final String METADATA_JSON_FILES_PATH = "./src/main/resources/output/metadata/json";
	
	@PostConstruct
	private void init() {
		System.out.println("[INFO] Creating RDF dataset");
		try {
			//Creates dataset in RDF store
			AuthenticationUtilities.createDataset(conn);
			connected = 1;
		}
		catch(Exception e) {
			System.out.println("[INFO] Couldn't reach FUSEKI");
		}
		
	}
	
	public void saveToRDFFile(String name, InputStream inputStream) throws SAXException, IOException {
		MetadataExtractor metadataExtractor = new MetadataExtractor();
		File directory = new File(METADATA_RDF_FILES_PATH);
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		System.out.println("[INFO] Writing to RDF file...");
		try {
			metadataExtractor.extractMetadata(
					inputStream, 
					new FileOutputStream(new File(METADATA_RDF_FILES_PATH + "/" + name)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveToJSONFile(String name, ArrayNode arrayNode) throws IOException {
		File directory = new File(METADATA_JSON_FILES_PATH);
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(METADATA_JSON_FILES_PATH + "/" + name), arrayNode);
	}
	
	public SearchByAuthorsResponseDTO searchMetadataByAuthor(String author) throws IOException {

		// Querying the named graph with a referenced SPARQL query
		System.out.println("[INFO] Loading SPARQL query from file \"" + SEARCH_BY_AUTHORS + "\"");
		String sparqlQuery = String.format(FileUtil.readFile(SEARCH_BY_AUTHORS, StandardCharsets.UTF_8),
				conn.dataEndpoint + SPARQL_NAMED_GRAPH_URI, author);
		
		System.out.println(sparqlQuery);
		
		// Create a QueryExecution that will access a SPARQL service over HTTP
		QueryExecution query = QueryExecutionFactory.sparqlService(conn.queryEndpoint, sparqlQuery);
		

		// Query the SPARQL endpoint, iterate over the result set...
		System.out.println("[INFO] Showing the results for SPARQL query using the result handler.\n");
		ResultSet results = query.execSelect();

		String varName;
		RDFNode varValue;
		
		SearchByAuthorsResponseDTO dto = new SearchByAuthorsResponseDTO();
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		while (results.hasNext()) {

			// A single answer from a SELECT query
			QuerySolution querySolution = results.next();
			Iterator<String> variableBindings = querySolution.varNames();
			
			String key = "";
			List<String> values = new ArrayList<String>();

			// Retrieve variable bindings
			while (variableBindings.hasNext()) {
				
				varName = variableBindings.next();
				varValue = querySolution.get(varName);
				
				if (varName.equals("author")) {
					String[] tokens = varValue.toString().split("/");
					key = tokens[tokens.length-1];
				}
				else {
					values.add(varValue.toString().split("\\^\\^")[0]);
				}

				System.out.println(varName + ": " + varValue);
				
			}
			
			if (!result.containsKey(key)) {
				result.put(key, values);
			}
			else {
				result.get(key).add(values.get(0));
			}
			
			System.out.println();
		}
		
		dto.setResult(result);
		query.close();

		System.out.println("[INFO] End.");
		
		return dto;
	}
	
	public void extractMetadata(String xmlContent, String fileName) throws IOException, SAXException, TransformerException {
		
		if (connected == 0) {
			AuthenticationUtilities.createDataset(conn);
			connected = 1;
		}
	
		System.out.println("[INFO] " + FusekiDocumentRepository.class.getSimpleName());
		
		
		
		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = new MetadataExtractor();
		
		InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
		
		this.saveToRDFFile(fileName + ".rdf", new ByteArrayInputStream(xmlContent.getBytes()));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		System.out.println("[INFO] Extracting metadata from RDFa attributes...");
		metadataExtractor.extractMetadata(
				inputStream, 
				outputStream);
				
		
		
		// Loading a default model with extracted metadata
		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(outputStream.toByteArray()), null);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream outXml = new ByteArrayOutputStream();
		
		model.write(out, SparqlUtil.NTRIPLES);
		model.write(outXml, SparqlUtil.RDF_XML);
		
		XmlMapper xmlMapper = new XmlMapper();
		//JsonNode node = xmlMapper.readValue(outXml.toByteArray(), JsonNode.class);
		//System.out.println(node.toString());
		ArrayNode json = xmlMapper.readValue(outXml.toByteArray(), ArrayNode.class);
		System.out.println(json.toString());
		this.saveToJSONFile(fileName + ".json", json);
		
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
