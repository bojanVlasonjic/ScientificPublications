package com.sp.ScientificPublications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		return new ResponseEntity<>(documentReviewService.retrieveDocumentReview(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<DocumentDTO> storeDocumentReview(@RequestBody DocumentDTO documentDTO) {
		return new ResponseEntity<>(documentReviewService.storeDocumentReview(documentDTO),
				HttpStatus.CREATED);
	}
	
	@PostMapping("/validate")
	public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(documentReviewService.validateDocumentReview(documentDTO.getDocumentContent()),
				HttpStatus.OK);
    }
	
	@PostMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {
        return new ResponseEntity<>(documentReviewService.generatePdf(id), HttpStatus.OK);
    }
}
