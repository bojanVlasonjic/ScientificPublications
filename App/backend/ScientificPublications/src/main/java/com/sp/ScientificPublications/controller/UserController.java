package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.service.logic.SubmitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private SubmitionService submitionService;

    @GetMapping("/submitions")
    public ResponseEntity getMySubmitions() {
        return new ResponseEntity<>(submitionService.mySubmitions(), HttpStatus.OK);
    }

    @Autowired
    private AuthorRepository authorRepository;

    @Secured({"ROLE_EDITOR"})
    @GetMapping("/editor")
    public String editor() {
        return "EDITOR ALLOWED";
    }

    @Secured({"ROLE_AUTHOR"})
    @GetMapping("/author")
    public String author() {
        return "AUTHOR ALLOWED";
    }

}
