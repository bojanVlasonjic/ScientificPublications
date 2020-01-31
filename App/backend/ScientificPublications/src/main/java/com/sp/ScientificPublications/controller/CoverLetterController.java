package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.models.cover_letter.CoverLetter;
import com.sp.ScientificPublications.service.CoverLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/cover-letter")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;


    @GetMapping("/template")
    public ResponseEntity<DocumentDTO> getCoverLetterTemplate() {
        return new ResponseEntity<>(coverLetterService.getTemplate(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<DocumentDTO> getCoverLetterById(@PathVariable String id) {
        return new ResponseEntity<>(coverLetterService.retrieveCoverLetterAsDocument(id), HttpStatus.OK);
    }

    @GetMapping("/jaxb/{id}")
    public  ResponseEntity<CoverLetter> getCoverLetterObjectId(@PathVariable String id) {
        return new ResponseEntity<>(coverLetterService.retrieveCoverLetterAsObject(id), HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<DocumentDTO> storeCoverLetter(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(coverLetterService.storeCoverLetterAsDocument(documentDTO), HttpStatus.CREATED);
    }



    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(coverLetterService.validateCoverLetter(document.getDocumentContent()), HttpStatus.OK);
    }
    
    @PostMapping("/validate-xml-file")
	public ResponseEntity<Boolean> validateCoverLetterFile(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<>(coverLetterService.validateCoverLetterXMLFile(file), HttpStatus.OK);
	}
    
    @PostMapping("/upload-xml-file")
    public ResponseEntity<DocumentDTO> uploadCoverLetterFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(coverLetterService.uploadDocumentReviewXMLFile(file), HttpStatus.OK);
    }



    @PostMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {

        return new ResponseEntity<>(
                coverLetterService.generatePdf(id),
                HttpStatus.OK);
    }


}
