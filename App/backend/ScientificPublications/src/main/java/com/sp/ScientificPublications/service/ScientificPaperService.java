package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.SearchByAuthorsResponseDTO;
import com.sp.ScientificPublications.dto.SendEmailDTO;
import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.repository.ReviewRepository;
import com.sp.ScientificPublications.repository.SubmitionRepository;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;
import com.sp.ScientificPublications.repository.exist.XQueryRepository;
import com.sp.ScientificPublications.repository.rdf.FusekiDocumentRepository;
import com.sp.ScientificPublications.utility.FileUtil;
import org.apache.xmlrpc.webserver.ServletWebServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.*;

@Service
public class ScientificPaperService {

    @Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;

    @Autowired
    FusekiDocumentRepository fusekiDocumentRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    XQueryRepository xQueryRepository;
    
    @Autowired
    SubmitionRepository submitionRepository;
    
    @Autowired
    UtilityService utilityService;

    @Autowired
    AuthorRepository authorRepository;
    
    @Autowired
    ReviewRepository reviewRepository;


    private static final String schemaPath = "src/main/resources/data/xsd_schema/scientific-paper.xsd";
    private static final String xslFoFilePath = "src/main/resources/data/xsl_fo/scientific-paper-fo.xsl";
    private static final String xsltFilePath = "src/main/resources/data/xslt/scientific-paper-xslt.xsl";
    private static final String templatePath = "src/main/resources/templates/scientific-paper-template.xml";

    private static final String PDF_DIRECTORY_PATH = "src/main/resources/output/pdf/scientific-paper/";
    private static final String HTML_DIRECTORY_PATH = "src/main/resources/output/html/scientific-paper/";

    private static final String collectionId = "/db/scientific-publication/scientific-papers";
    private static final String modelPackage = "com.sp.ScientificPublications.models.scientific_paper";
    private static final String SPARQL_NAMED_GRAPH_URI = "/scientific-paper/sparql/metadata";

    // ===================== DOWNLOAD =====================
    public ResponseEntity<InputStreamResource> downloadPDF(String xmlId) throws IOException {
        String path = PDF_DIRECTORY_PATH + xmlId + ".pdf";
        File file = new File(path);
        if (!file.exists()) throw new ApiBadRequestException("No such file");
        
        HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("attachment", file.getName());
        
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(inputStream);
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    public ResponseEntity<InputStreamResource> downloadHTML(String xmlId) throws IOException {
        String path = HTML_DIRECTORY_PATH + xmlId + ".html";
        File file = new File(path);
        if (!file.exists()) throw new ApiBadRequestException("No such file");
        
        HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/html"));
        headers.setContentLength(file.length());
        headers.setContentDispositionFormData("attachment", file.getName());
        
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(inputStream);
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    public ResponseEntity<InputStreamResource> downloadXML(String xmlId) throws XMLDBException {
    	XMLResource res = existDocumentRepo.retrieveXmlFile(collectionId, xmlId);
    	
    	InputStreamResource resource = new InputStreamResource(
    			new ByteArrayInputStream(res.getContent().toString().getBytes()));
    	
    	HttpHeaders headers = utilityService.getDownloadHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml"));
        headers.setContentDispositionFormData("attachment", xmlId + ".xml");
    	
    	return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
 	// =========================================================================

    public List<String> getAllReferencesTowardsScientificPaper(String xmlId) {
    	String queryFilePath = "./src/main/resources/data/xquery/get-scientific-paper-references.xqy";
    	String query = "";
		try {
			query = FileUtil.readFile(queryFilePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	List<String> references = new ArrayList<String>();
    	
    	List<Submition> submitions = this.submitionRepository.findAllByStatus(SubmitionStatus.PUBLISHED);
    	for (Submition sub : submitions) {
    		List<String> inDocumentReferences = this.xQueryRepository.getAllReferencesFromScientificPaper(collectionId, query, sub.getPaperId());
    		for (String ref : inDocumentReferences) {
    			String[] tokens = ref.split("/");
    			if (tokens.length >= 3) {
    				String refId = tokens[tokens.length - 1];
    				String collectionId = tokens[tokens.length - 3];
    				if (collectionId.equals("scientific-paper") &&
    						refId.equals(xmlId) &&
    						!references.contains(sub.getPaperId())) {
    					references.add(sub.getPaperId());
    				}
    			}
    		}
    	}
    	
    	return references;
    }
    
    // ============================== VIEW ==============================
    public ResponseEntity<byte[]> viewScientificPaper(String id) throws URISyntaxException, IOException {
    	
    	String filename = PDF_DIRECTORY_PATH + id + ".pdf";
    	File pdf = new File(filename);
    	if (!pdf.exists()) throw new ApiBadRequestException("No such file");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.parseMediaType("application/pdf"));
    	headers.add("content-disposition", "inline;filename=" + filename);
    	headers.setCacheControl("must-revalidate, post-check=0 pre-check=0");
    	
    	byte[] file = xmlTransformSvc.loadFile(filename);
    	
    	return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
    }
    // ============================================================
    
    public DocumentDTO getTemplate() {

        DocumentDTO templateDTO = new DocumentDTO();
        try {
            templateDTO.setDocumentContent(FileUtil.readFile(templatePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiBadRequestException("Failed to generate scientific paper template");
        }

        return templateDTO;
    }

    public boolean sendEmail(SendEmailDTO dto) {

        String pdfPath = PDF_DIRECTORY_PATH + dto.getPdfGUID() + ".pdf";
        String htmlPath = HTML_DIRECTORY_PATH + dto.getHtmlGUID() + ".html";

        byte[] pdfBytes = null;
        byte[] htmlBytes = null;

        try {
            pdfBytes = xmlTransformSvc.loadFile(pdfPath);
            htmlBytes = xmlTransformSvc.loadFile(htmlPath);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


        Map<String, ByteArrayResource> attachmentNameAndByteResource = new HashMap<String, ByteArrayResource>();
        attachmentNameAndByteResource.put("ScientificPaper.pdf", new ByteArrayResource(pdfBytes));
        attachmentNameAndByteResource.put("ScientificPaper.html", new ByteArrayResource(htmlBytes));

        emailSenderService.sendMessageWithAttachments(
                dto.getTo(),
                dto.getSubject(),
                dto.getMessage(),
                attachmentNameAndByteResource);
        return true;
    }

    // ================= File manipulation

    public boolean validateScientificPaperXMLFile(MultipartFile file) {
        return this.validateScientificPaper(domParserSvc.readMultipartXMLFile(file));
    }

    public DocumentDTO uploadScientificPaperXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();
    	document.setDocumentContent(xmlContent);
    	document = this.storeScientificPaperAsDocument(document);
    	this.generatePdf(document.getDocumentId());
    	this.generateHtml(document.getDocumentId());
    	
    	return document;
    }

    // =================

    public boolean validateScientificPaper(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }

    public String generatePdf(String documentId) {
        DocumentDTO retrievedDTO = this.retrieveScientificPaperAsDocument(documentId);
        retrievedDTO.setDocumentId("scientific-paper/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFoFilePath);
    }

    public String generateHtml(String documentId) {
        DocumentDTO retrievedDTO = this.retrieveScientificPaperAsDocument(documentId);
        retrievedDTO.setDocumentId("scientific-paper/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generateHtmlFromXml(retrievedDTO, xsltFilePath);
    }

    public SearchByAuthorsResponseDTO searchMetadataByAuthor(String author) throws IOException {
        return fusekiDocumentRepository.searchMetadataByAuthor(author);
    }

    //fileName without extension
    public void extractMetaData(String xmlContent, String fileName) throws IOException, SAXException, TransformerException {
        fusekiDocumentRepository.extractMetadata(xmlContent, fileName);
    }


    public ScientificPaper retrieveScientificPaperAsObject(String documentId) {

        try {
            return (ScientificPaper) existJaxbRepo.retrieveObject(collectionId, documentId, modelPackage);
        } catch (XMLDBException | JAXBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve scientific paper");
        }

    }


    public DocumentDTO retrieveScientificPaperAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the scientific publication");
        }

        return document;
    }


    // TODO: implement an update method, change method name
    public ScientificPaper storeScientificPaperAsObject(String documentId) {

        ScientificPaper scPaper = retrieveScientificPaperAsObject(documentId);

        //TODO: update data
        scPaper.getHeader().setTitle("Setter in a getter, woohoo!");

        try {
            return (ScientificPaper) existJaxbRepo.storeObject(collectionId, documentId, modelPackage, scPaper);
        } catch (XMLDBException | JAXBException ex) {
            throw new ApiBadRequestException("Failed to store scientific paper");
        }

    }


    public DocumentDTO storeScientificPaperAsDocument(DocumentDTO document) {

        if (!validateScientificPaper(document.getDocumentContent())) {
            throw new ApiBadRequestException("The scientific publication is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existDocumentRepo.storeXmlFile(collectionId, document);
            document.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to save scientific publication");
        }
        return document;
    }

    public String searchByKeyword() {
        try {
            String collectionId = "/db/scientific-publication/scientific-papers";
            String path = "/home/aes/Desktop/ScientificPublications/App/backend/ScientificPublications/src/main/resources/data/xquery/get-scientific-paper-by-keywords.xgy";
            String xQueryExpression = FileUtil.readFile(path, StandardCharsets.UTF_8);
            ResourceSet resourceSet = xQueryRepository.find(collectionId, xQueryExpression);
            return String.valueOf(resourceSet.getSize());
        } catch (Exception ex) {
            throw new ApiBadRequestException("Something went wrong...");
        }
    }
    
    private HttpHeaders getDownloadHeaders() {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
    	return headers;
    }

    public List<UserDTO> getRecommendedReviewers(String paperId) {
        List<UserDTO> rankedUsers = null;
        try {
        	Submition submition = submitionRepository.findByPaperId(paperId);
        	
            // get xml paper
            ScientificPaper scientificPaper = retrieveScientificPaperAsObject(paperId);

            // tokenize keywords from paper
            Set<String> paperKeywords = tokenizeKeywords(scientificPaper.getAbstract().getKeywords());

            // get distinct keywords for every author
            Map<String, Set<String>> authorsKeywords = getDistinctKeywordsForAuthors(scientificPaper, submition);

            // rank authors keywords against paper keywords
            rankedUsers = rankAuthors(paperKeywords, authorsKeywords);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rankedUsers;
    }

    public List<UserDTO> rankAuthors(Set<String> paperKeywords, Map<String, Set<String>> authorsKeywords) {
        List<UserDTO> rankings = new ArrayList<>();
        for (String authorEmail : authorsKeywords.keySet()) {
            authorsKeywords.get(authorEmail).retainAll(paperKeywords);
            Integer rank = authorsKeywords.get(authorEmail).size();
            UserDTO userDTO = new UserDTO(authorRepository.findByEmail(authorEmail).get());
            userDTO.setRank(rank);
            rankings.add(userDTO);
        }
        return rankings;
    }

    public Map<String, Set<String>> getDistinctKeywordsForAuthors(ScientificPaper scientificPaper, Submition submition) throws Exception {
        // get authors of given scientific paper
        Set<String> authorsOfPaper = getAuthorsForDocument(scientificPaper.getHeader().getTitle());
        
        List<String> reviewers = submition.getReviewers().stream().map(Author::getEmail).collect(Collectors.toList());
        List<String> requestedReviewers = submition.getRequestedReviewers().stream().map(Author::getEmail).collect(Collectors.toList());

        // filter all authors against authorsOfPaper
        List<Author> filteredAuthors = authorRepository.findAll()
                .stream()
                .filter(author -> !authorsOfPaper.contains(author.getEmail()))
                .filter(author -> !reviewers.contains(author.getEmail()))
                .filter(author -> !requestedReviewers.contains(author.getEmail()))
                .collect(Collectors.toList());

        // get all distinct keywords for every filtered author
        Map<String, Set<String>> authorsKeywords = new HashMap<>();
        for (Author author : filteredAuthors) {
            String authorEmail = author.getEmail();
            Set<String> keywords = getKeywordsForAuthor(authorEmail);

            if (authorsKeywords.containsKey(authorEmail)) {
                authorsKeywords.get(authorEmail).addAll(keywords);
            } else {
                authorsKeywords.put(authorEmail, keywords);
            }
        }
        return authorsKeywords;
    }

    public Set<String> getKeywordsForAuthor(String authorEmail) throws Exception {
        String keywordsTemplatePath = "src/main/resources/data/xquery/get-author-keywords.xqy";
        String collectionId = "/db/scientific-publication/scientific-papers/";
        String keywordsTemplate = FileUtil.readFile(keywordsTemplatePath, StandardCharsets.UTF_8);
        String keywordsXQuery = String.format(keywordsTemplate, authorEmail);

        ResourceSet resourceSet = xQueryRepository.find(collectionId, keywordsXQuery);
        ResourceIterator resourceIterator = resourceSet.getIterator();

        Set<String> results = new HashSet<>();
        while (resourceIterator.hasMoreResources()) {
            Resource resource = resourceIterator.nextResource();
            results.addAll(tokenizeKeywords(resource.getContent().toString()));
        }
        return results;
    }

    public Set<String> tokenizeKeywords(String keywords) {
        Set<String> results = new HashSet<>();
        keywords = keywords.replace(" ", "");
        results.addAll(Arrays.asList(keywords.split(",")));
        return results;
    }

    public Set<String> getAuthorsForDocument(String documentTitle) throws Exception {
        String keywordsTemplatePath = "src/main/resources/data/xquery/get-authors-from-document.xqy";
        String collectionId = "/db/scientific-publication/scientific-papers/";
        String keywordsTemplate = FileUtil.readFile(keywordsTemplatePath, StandardCharsets.UTF_8);
        String keywordsXQuery = String.format(keywordsTemplate, documentTitle);

        ResourceSet resourceSet = xQueryRepository.find(collectionId, keywordsXQuery);
        ResourceIterator resourceIterator = resourceSet.getIterator();

        Set<String> results = new HashSet<>();
        while (resourceIterator.hasMoreResources()) {
            Resource resource = resourceIterator.nextResource();
            results.addAll(tokenizeKeywords(resource.getContent().toString()));
        }
        return results;
    }

    public void simpleSearch(String query) throws Exception {
        String simpleSearchTemplatePath = "src/main/resources/data/xquery/simple-search.sqy";
        String collectionId = "/db/scientific-publication/scientific-papers/";
        String keywordsTemplate = FileUtil.readFile(simpleSearchTemplatePath, StandardCharsets.UTF_8);
        String simpleSearchQuery = String.format(keywordsTemplate, query);


        ResourceSet resourceSet = xQueryRepository.find(collectionId, simpleSearchQuery);
        ResourceIterator resourceIterator = resourceSet.getIterator();

        while (resourceIterator.hasMoreResources()) {
            Resource resource = resourceIterator.nextResource();
        }

    }
}
