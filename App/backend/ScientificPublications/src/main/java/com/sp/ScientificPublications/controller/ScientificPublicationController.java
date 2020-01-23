package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.DocumentPathDTO;
import com.sp.ScientificPublications.service.ScientificPublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/scientific-publication")
public class ScientificPublicationController {

    @Autowired
    ScientificPublicationService scPublService;

    @GetMapping("/{id}")
    public  ResponseEntity<DocumentDTO> getScientificPublicationById(@PathVariable String id) {
        return new ResponseEntity<>(scPublService.retrieveScientificPublication(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> storeScientificPublication(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(scPublService.storeScientificPublication(documentDTO), HttpStatus.CREATED);
    }


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateScientificPublication(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(scPublService.validateScientificPublication(document.getDocumentContent()),
                HttpStatus.OK);
    }
}
