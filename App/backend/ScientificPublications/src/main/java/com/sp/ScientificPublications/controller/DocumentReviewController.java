package com.sp.ScientificPublications.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import com.sp.ScientificPublications.models.document_review.DocumentReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.DocumentReviewService;

@RestController
@RequestMapping("api/document-review")
public class DocumentReviewController {
	
	@Autowired
	DocumentReviewService documentReviewService;


	@GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewScientificPaper(@PathVariable String id) {
    	try {
			return documentReviewService.viewDocumentReview(id);
		} catch (URISyntaxException | IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@GetMapping("/template")
	public ResponseEntity<DocumentDTO> getDocumentReviewTemplate() {
		return new ResponseEntity<>(documentReviewService.getTemplate(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocumentDTO> getDocumentReviewById(@PathVariable String id) {
		return new ResponseEntity<>(documentReviewService.retrieveDocumentReviewAsDocument(id), HttpStatus.OK);
	}

	@GetMapping("/jaxb/{id}")
	public ResponseEntity<DocumentReview> getDocumentReviewObjectById(@PathVariable String id) {
		return new ResponseEntity<>(documentReviewService.retrieveDocumentReviewAsObject(id), HttpStatus.OK);
	}


	@PostMapping
	public ResponseEntity<DocumentDTO> storeDocumentReview(@RequestBody DocumentDTO documentDTO) {
		return new ResponseEntity<>(documentReviewService.storeDocumentReviewAsDocument(documentDTO),
				HttpStatus.CREATED);
	}



	@PostMapping("/validate")
	public ResponseEntity<Boolean> validateDocumentReview(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(documentReviewService.validateDocumentReview(document.getDocumentContent()), HttpStatus.OK);
	}

	@PostMapping("/validate-xml-file")
	public ResponseEntity<Boolean> validateDocumentReviewFile(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(documentReviewService.validateDocumentReviewXMLFile(file), HttpStatus.OK);
	}
	
	@PostMapping("/upload-xml-file")
    public ResponseEntity<DocumentDTO> uploadDocumentReviewFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(documentReviewService.uploadDocumentReviewXMLFile(file), HttpStatus.OK);
    }
	


	@GetMapping("/pdf/{id}")
  	public ResponseEntity<String> generatePdf(@PathVariable String id) {
		return new ResponseEntity<>(documentReviewService.generatePdf(id), HttpStatus.OK);
  	}

	@GetMapping("/html/{id}")
	public ResponseEntity<String> generateHtml(@PathVariable String id) {
		return new ResponseEntity<>(documentReviewService.generateHtml(id), HttpStatus.OK);
	}
  
}
