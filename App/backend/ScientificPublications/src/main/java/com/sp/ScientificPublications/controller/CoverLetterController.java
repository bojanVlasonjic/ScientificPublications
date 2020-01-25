package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.CoverLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cover-letter")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;

    @GetMapping("/{id}")
    public  ResponseEntity<DocumentDTO> getCoverLetterById(@PathVariable String id) {
        return new ResponseEntity<>(coverLetterService.retrieveCoverLetter(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> storeCoverLetter(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(coverLetterService.storeCoverLetter(documentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(coverLetterService.validateCoverLetter(document.getDocumentContent()), HttpStatus.OK);
    }

    @PostMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {

        return new ResponseEntity<>(
                coverLetterService.generatePdf(id),
                HttpStatus.OK);
    }


}
