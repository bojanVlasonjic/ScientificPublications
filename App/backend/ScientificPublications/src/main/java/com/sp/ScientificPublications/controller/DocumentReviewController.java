package com.sp.ScientificPublications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.DocumentPathDTO;
import com.sp.ScientificPublications.service.DocumentReviewService;

@RestController
@RequestMapping("api/document-review")
public class DocumentReviewController {
	
	@Autowired
	DocumentReviewService documentReviewService;
	
	@PostMapping("/validate")
	public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(documentReviewService.validateDocumentReview(document.getDocumentContent()), HttpStatus.OK);
    }
	
	@PostMapping("/generate-pdf")
    public ResponseEntity<String> generatePdf(@RequestBody DocumentPathDTO documentPath) {
        return new ResponseEntity<>(
        		documentReviewService.generatePdf(documentPath.getPath()),
                HttpStatus.OK);
    }
}
