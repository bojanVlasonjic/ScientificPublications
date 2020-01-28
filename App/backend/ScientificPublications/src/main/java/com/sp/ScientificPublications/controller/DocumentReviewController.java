package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.models.document_review.DocumentReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.DocumentReviewService;

@RestController
@RequestMapping("api/document-review")
public class DocumentReviewController {
	
	@Autowired
	DocumentReviewService documentReviewService;

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
	


	@PostMapping("/pdf/{id}")
  	public ResponseEntity<String> generatePdf(@PathVariable String id) {
		return new ResponseEntity<>(documentReviewService.generatePdf(id), HttpStatus.OK);
  	}
  
}
