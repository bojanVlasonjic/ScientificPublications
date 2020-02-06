package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.dto.submitions.AuthorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.CreateSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.SubmitionViewDTO;
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
import java.util.List;

@RestController
@RequestMapping("api/submitions")
public class SubmitionController {

    @Autowired
    private SubmitionService submitionService;
    
    @Secured({"ROLE_EDITOR", "ROLE_AUTHOR"})
    @GetMapping("/requested-reviewers/{paperId}")
    public ResponseEntity<List<UserDTO>> getRequestedReviewers(@PathVariable String paperId) {
    	return new ResponseEntity<>(submitionService.getRequestedReviewers(paperId), HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @GetMapping
    public ResponseEntity getSubmitions() {
        Pageable pageable = PageRequest.of(0, 1000000);
        return new ResponseEntity(submitionService.getSubmitions(pageable), HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<List<SubmitionViewDTO>> getPublishedSubmitions() {
        return new ResponseEntity<>(submitionService.getPublishedSubmitions(), HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @GetMapping("/author")
    public ResponseEntity<List<AuthorSubmitionDTO>> getSubmitionsForAuthor() {
        return new ResponseEntity<>(submitionService.mySubmitions(), HttpStatus.OK);
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
    public ResponseEntity<AuthorSubmitionDTO> cancelSubmition(@PathVariable Long id) {
        return new ResponseEntity<>(submitionService.cancelSubmition(id), HttpStatus.OK);
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
    @PutMapping("/{submitionId}/reject")
    public ResponseEntity rejectSubmition(@PathVariable Long submitionId) {
        submitionService.rejectSubmition(submitionId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{id}/request-revision")
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

    @Secured({"ROLE_EDITOR"})
    @PutMapping("/{submitionId}/requested-reviewers/{reviewerId}/cancel")
    public ResponseEntity cancelRequestReview(@PathVariable Long submitionId, @PathVariable Long reviewerId) {
        submitionService.cancelRequestReview(submitionId, reviewerId);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @Secured({"ROLE_EDITOR"})
    @GetMapping("/{submitionId}/reviewers")
    public ResponseEntity<List<UserDTO>> getAllReviewersForSubmition(@PathVariable Long submitionId) {
        return new ResponseEntity<>(submitionService.getAllReviewersForSubmition(submitionId), HttpStatus.OK);
    }
}
