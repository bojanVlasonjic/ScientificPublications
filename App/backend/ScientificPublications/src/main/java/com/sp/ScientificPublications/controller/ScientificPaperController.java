package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.service.ScientificPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/scientific-paper")
public class ScientificPaperController {

    @Autowired
    ScientificPaperService scPaperService;

    @GetMapping("/{id}")
    public  ResponseEntity<DocumentDTO> getScientificPaperById(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.retrieveScientificPaper(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> storeScientificPaper(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(scPaperService.storeScientificPaper(documentDTO), HttpStatus.CREATED);
    }


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateScientificPaper(@RequestBody DocumentDTO document) {
        return new ResponseEntity<>(scPaperService.validateScientificPaper(document.getDocumentContent()),
                HttpStatus.OK);
    }
    
    @PostMapping("/validate-xml-file")
    public ResponseEntity<Boolean> validateScientificPaperFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(scPaperService.validateScientificPaperXMLFile(file), HttpStatus.OK);
    }
    

    @PostMapping("/pdf/{id}")
    public ResponseEntity<String> generatePdf(@PathVariable String id) {
        return new ResponseEntity<>(scPaperService.generatePdf(id), HttpStatus.OK);
    }
  
}
