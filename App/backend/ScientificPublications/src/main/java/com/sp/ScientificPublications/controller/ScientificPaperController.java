package com.sp.ScientificPublications.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.SearchByAuthorsResponseDTO;
import com.sp.ScientificPublications.dto.XmlFileUploadResponseDTO;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import com.sp.ScientificPublications.service.DomParserService;
import com.sp.ScientificPublications.service.ScientificPaperService;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("api/scientific-paper")
public class ScientificPaperController {

    @Autowired
    ScientificPaperService scPaperService;
    
    @Autowired
    DomParserService domParserSvc;

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



    @PostMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.generatePdf(id), HttpStatus.OK);
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
    
    
}
