package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.DomParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dom-parser")
public class DomParserController {

    @Autowired
    DomParserService docValidatorSvc;

    @PostMapping("/parse")
    public ResponseEntity<Boolean> parseXmlDocument(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(docValidatorSvc.parseXmlDocument(documentDTO), HttpStatus.OK);
    }
}
