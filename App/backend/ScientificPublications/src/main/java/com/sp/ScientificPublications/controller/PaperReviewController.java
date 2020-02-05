package com.sp.ScientificPublications.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import com.sp.ScientificPublications.dto.reviews.CreateReviewDTO;
import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.PaperReviewService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/paper-review")
public class PaperReviewController {
	
	@Autowired
	PaperReviewService paperReviewService;
	
	@GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewScientificPaper(@PathVariable String id) {
    	try {
			return paperReviewService.viewPaperReview(id);
		} catch (URISyntaxException | IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@PostMapping("/validate-xml-file")
	public ResponseEntity<Boolean> validateDocumentReviewFile(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(paperReviewService.validatePaperReviewXMLFile(file), HttpStatus.OK);
	}
	
	@PostMapping("/upload-xml-file")
    public ResponseEntity<DocumentDTO> uploadDocumentReviewFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(paperReviewService.uploadPaperReviewXMLFile(file), HttpStatus.OK);
    }

    @GetMapping("/to-rdf/{id}")
	public String saveToRdf(@PathVariable String id) {
		paperReviewService.saveToRdf(id);
		return "123";
	}

	@Secured("ROLE_AUTHOR")
	@PostMapping
	public ResponseEntity<ReviewDTO> createReview(@RequestBody @Valid CreateReviewDTO createReviewDTO) {
		return new ResponseEntity<>(paperReviewService.createReview(createReviewDTO), HttpStatus.OK);
	}

}
