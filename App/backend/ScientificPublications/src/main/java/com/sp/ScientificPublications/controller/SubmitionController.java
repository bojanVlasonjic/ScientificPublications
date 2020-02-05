package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.submitions.CreateSubmitionDTO;
import com.sp.ScientificPublications.service.logic.SubmitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("api/submitions")
public class SubmitionController {

    @Autowired
    private SubmitionService submitionService;

    @Secured({"ROLE_EDITOR"})
    @GetMapping
    public ResponseEntity getSubmitions() {
        Pageable pageable = PageRequest.of(0, 1000000);
        return new ResponseEntity(submitionService.getSubmitions(pageable), HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PostMapping
    public ResponseEntity createSubmition(@RequestBody @Valid CreateSubmitionDTO createSubmitionDTO) {
        submitionService.createSubmition(createSubmitionDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Secured({"ROLE_AUTHOR"})
    @PostMapping("/file")
    public ResponseEntity createSubmitionFile(@RequestParam("submition-files")
                                                          MultipartFile[] files) {
        submitionService.createSubmitionFile(files);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Secured({"ROLE_AUTHOR"})
    @DeleteMapping("/{id}")
    public ResponseEntity cancelSubmition(@PathVariable Long id) {
        submitionService.cancelSubmition(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PutMapping("/{id}")
    public ResponseEntity reviseSubmition(@PathVariable Long id, @RequestBody @Valid CreateSubmitionDTO revisedSubmitonDTO) {
        submitionService.reviseSubmition(id, revisedSubmitonDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PutMapping("/{id}/approve")
    public ResponseEntity approveSubmition(@PathVariable Long id) {
        submitionService.approveSubmition(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{id}/reject")
    public ResponseEntity rejectSubmition(@PathVariable Long id) {
        submitionService.rejectSubmition(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{id]/request-revision")
    public ResponseEntity requestRevision(@PathVariable Long id) {
        submitionService.requestRevision(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{id}/publish")
    public ResponseEntity publishRevision(@PathVariable Long id) {
        submitionService.publishSubmition(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{submitionId}/requested-reviewers/{reviewerId}")
    public ResponseEntity requestReview(@PathVariable Long submitionId, @PathVariable Long reviewerId) {
        submitionService.requestReview(submitionId, reviewerId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
