package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentPathDTO;
import com.sp.ScientificPublications.service.CoverLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cover-letter")
public class CoverLetterController {

    @Autowired
    CoverLetterService coverLetterService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateCoverLetter(@RequestBody DocumentPathDTO documentPathDTO) {
        return new ResponseEntity<>(coverLetterService.validateCoverLetter(documentPathDTO.getPath()), HttpStatus.OK);
    }
}
