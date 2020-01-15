package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentPathDTO;
import com.sp.ScientificPublications.service.ScientificPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/scientific-publication")
public class ScientificPublicationController {

    @Autowired
    ScientificPublicationService scPublService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateScientificPublication(@RequestBody  DocumentPathDTO documentPathDTO) {
        return new ResponseEntity<>(scPublService.validateScientificPublication(documentPathDTO.getPath()), HttpStatus.OK);
    }
}
