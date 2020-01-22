package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentPathDTO;
import com.sp.ScientificPublications.service.CoverLetterService;
import com.sp.ScientificPublications.service.XmlTransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cover-letter")
public class CoverLetterController {

    @Autowired
    private CoverLetterService coverLetterService;


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentPathDTO documentPath) {
        return new ResponseEntity<>(coverLetterService.validateCoverLetter(documentPath.getPath()), HttpStatus.OK);
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<String> generatePdf(@RequestBody DocumentPathDTO documentPath) {

        return new ResponseEntity<>(
                coverLetterService.generatePdf(documentPath.getPath()),
                HttpStatus.OK);

    }
}
