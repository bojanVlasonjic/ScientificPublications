package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import com.sp.ScientificPublications.service.logic.SubmitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/editor")
public class EditorController {

    @Autowired
    private SubmitionService submitionService;

    @Secured("ROLE_EDITOR")
    @GetMapping("/reviews/{submitionId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForSubmition(@PathVariable Long submitionId) {
        return new ResponseEntity<>(submitionService.getReviewsForSubmitionEditor(submitionId), HttpStatus.OK);
    }

    @Secured("ROLE_EDITOR")
    @GetMapping("/set-in-review-process/{submitionId}")
    public ResponseEntity setSubmitionStatusInReviewProcess(@PathVariable Long submitionId) {
        submitionService.setSubmitionStatusInReviewProcess(submitionId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
