package com.sp.ScientificPublications.controller;

import com.sp.ScientificPublications.dto.PageableResultsDTO;
import com.sp.ScientificPublications.dto.reviews.CreateReviewDTO;
import com.sp.ScientificPublications.dto.reviews.PendingReviewDTO;
import com.sp.ScientificPublications.dto.submitions.EditorSubmitionDTO;
import com.sp.ScientificPublications.service.logic.ReviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reviewers")
public class ReviewerController {

    @Autowired
    private ReviewerService reviewerService;
    
    @Secured({"ROLE_AUTHOR"})
    @GetMapping("/pending/reviews")
    public ResponseEntity<List<PendingReviewDTO>> getPendingReviewsForCurrentReviewer() {
    	return new ResponseEntity<>(reviewerService.getPendingReviewsForCurrentReviewer(), HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PutMapping("/submitions/{id}/accept")
    public ResponseEntity acceptSubmitionReviewRequest(@PathVariable Long id) {
        reviewerService.acceptSubmitionReviewRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PutMapping("/submitions/{id}/reject")
    public ResponseEntity rejectSubmitionReviewRequest(@PathVariable Long id) {
        reviewerService.rejectSubmitionReviewRequest(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @GetMapping("/submitions")
    public ResponseEntity getAcceptedSubmitionReviews(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(reviewerService.getMySubmitions(pageable), HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @GetMapping("/requested-submitions")
    public ResponseEntity getRequestedSubmitionReviews(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(reviewerService.getMyRequestedSubmitions(pageable), HttpStatus.OK);
    }

    @Secured({"ROLE_AUTHOR"})
    @PostMapping
    public ResponseEntity createReview(@RequestBody @Valid CreateReviewDTO createReviewDTO) {
        reviewerService.createReview(createReviewDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
