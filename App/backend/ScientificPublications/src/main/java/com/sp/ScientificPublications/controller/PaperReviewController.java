package com.sp.ScientificPublications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.PaperReviewService;

@RestController
@RequestMapping("api/paper-review")
public class PaperReviewController {
	
	@Autowired
	PaperReviewService paperReviewService;

	@PostMapping("/validate-xml-file")
	public ResponseEntity<Boolean> validateDocumentReviewFile(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(paperReviewService.validatePaperReviewXMLFile(file), HttpStatus.OK);
	}
	
	@PostMapping("/upload-xml-file")
    public ResponseEntity<DocumentDTO> uploadDocumentReviewFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(paperReviewService.uploadPaperReviewXMLFile(file), HttpStatus.OK);
    }


	
}
