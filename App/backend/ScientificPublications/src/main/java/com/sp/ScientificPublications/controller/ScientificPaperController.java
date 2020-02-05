package com.sp.ScientificPublications.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.SearchByAuthorsResponseDTO;
import com.sp.ScientificPublications.dto.SendEmailDTO;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import com.sp.ScientificPublications.service.DomParserService;
import com.sp.ScientificPublications.service.ScientificPaperService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

@RestController
@RequestMapping("api/scientific-paper")
public class ScientificPaperController {

    @Autowired
    ScientificPaperService scPaperService;
    
    @Autowired
    DomParserService domParserSvc;
    
    
    
    @GetMapping("/get/referenced-by/{id}")
    public ResponseEntity<List<String>> getReferencedBy(@PathVariable String id) {
    	return new ResponseEntity<List<String>>(scPaperService.getAllReferencesTowardsScientificPaper(id), HttpStatus.OK);
    }
    
    @GetMapping("/download/xml/{id}")
    public ResponseEntity<InputStreamResource> downloadXML(@PathVariable String id) { 
    	try {
			return scPaperService.downloadXML(id);
		} catch (XMLDBException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("/download/html/{id}")
    public ResponseEntity<InputStreamResource> downloadHTML(@PathVariable String id) { 
    	try {
			return scPaperService.downloadHTML(id);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("/download/pdf/{id}")
    public ResponseEntity<InputStreamResource> downloadPDF(@PathVariable String id) { 
    	try {
			return scPaperService.downloadPDF(id);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewScientificPaper(@PathVariable String id) {
    	try {
			return scPaperService.viewScientificPaper(id);
		} catch (URISyntaxException | IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    @GetMapping("/keywords")
    public ResponseEntity getByKeywords() throws ClassNotFoundException, InstantiationException, XMLDBException, IllegalAccessException {
        return new ResponseEntity<>(scPaperService.searchByKeyword(), HttpStatus.OK);
    }

    @GetMapping("/template")
    public ResponseEntity<DocumentDTO> getScientificPaperTemplate() {
        return new ResponseEntity<>(scPaperService.getTemplate(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<DocumentDTO> getScientificPaperById(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.retrieveScientificPaperAsDocument(id), HttpStatus.OK);
    }

    @GetMapping("jaxb//{id}")
    public  ResponseEntity<ScientificPaper> getScientificPaperObjectById(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.retrieveScientificPaperAsObject(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> storeScientificPaper(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(scPaperService.storeScientificPaperAsDocument(documentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateScientificPaper(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(scPaperService.validateScientificPaper(document.getDocumentContent()),
                HttpStatus.OK);
    }
    
    @PostMapping("/validate-xml-file")
    public ResponseEntity<Boolean> validateScientificPaperFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(scPaperService.validateScientificPaperXMLFile(file), HttpStatus.OK);
    }
    
    @PostMapping("/upload-xml-file")
    public ResponseEntity<DocumentDTO> uploadScientificPaperFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(scPaperService.uploadScientificPaperXMLFile(file), HttpStatus.OK);
    }

    @PostMapping("/send-email")
    public ResponseEntity<Boolean> sendEmail(@RequestBody SendEmailDTO sendEmailDTO) {
    	return new ResponseEntity<>(scPaperService.sendEmail(sendEmailDTO), HttpStatus.OK);
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.generatePdf(id), HttpStatus.OK);
    }

    @GetMapping("/html/{id}")
    public ResponseEntity<String> generateHtml(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.generateHtml(id), HttpStatus.OK);
    }
  
    @PostMapping("/rdf/extract")
    public ResponseEntity<String> extractMetadata(@RequestParam("file") MultipartFile file) throws IOException, SAXException, TransformerException {
    	scPaperService.extractMetaData(domParserSvc.readMultipartXMLFile(file), "test");
    	return new ResponseEntity<>("Test success", HttpStatus.OK);
    }
    
    @PostMapping("/rdf/search/by-authors")
    public ResponseEntity<SearchByAuthorsResponseDTO> searchByAuthors(@RequestBody JsonNode node) {
    	try {
			return new ResponseEntity<>(scPaperService.searchMetadataByAuthor(
					node.get("author").asText()),
					HttpStatus.OK);
		} catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //@Secured({"ROLE_EDITOR"})
    @GetMapping("/{paperId}/recommended-reviewers")
    public ResponseEntity getRecommendedReviewers(@PathVariable String paperId) {
        return new ResponseEntity(scPaperService.getRecommendedReviewers(paperId), HttpStatus.OK);
    }
}
