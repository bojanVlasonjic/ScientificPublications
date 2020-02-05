package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.dto.PageableResultsDTO;
import com.sp.ScientificPublications.dto.reviews.CreateReviewDTO;
import com.sp.ScientificPublications.dto.reviews.PendingReviewDTO;
import com.sp.ScientificPublications.dto.submitions.AuthorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.EditorSubmitionDTO;
import com.sp.ScientificPublications.exception.ApiInternalServerException;
import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.repository.ReviewRepository;
import com.sp.ScientificPublications.repository.SubmitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewerService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SubmitionRepository submitionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccessControlService accessControlService;

    @Autowired
    private AuthenticationService authenticationService;
    
    public List<PendingReviewDTO> getPendingReviewsForCurrentReviewer() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String email = authentication.getName();
    	
    	Optional<Author> optReviewer = authorRepository.findByEmail(email);
    	if (!optReviewer.isPresent()) throw new ApiInternalServerException("No such user");
    	
    	Author reviewer = optReviewer.get();
    	
    	List<Submition> pendingSubmitions = submitionRepository.findAllByRequestedReviewersContaining(reviewer);
    	
    	List<PendingReviewDTO> pendingReviewsDTO = pendingSubmitions.stream().map(x -> new PendingReviewDTO(x)).collect(Collectors.toList());
    	
    	return pendingReviewsDTO;
    }

    public void acceptSubmitionReviewRequest(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = authenticationService.getCurrentAuthor();
            accessControlService.checkIfUserIsRequestedToReviewSubmition(reviewer, submition);
            submition.getRequestedReviewers().removeIf(requestedReviewer -> requestedReviewer.getId() == reviewer.getId());
            reviewer.getRequestedSubmitions().removeIf(submitionRequest -> submitionRequest.getId() == submition.getId());
            submition.getReviewers().add(reviewer);
            reviewer.getSubmitions().add(submition);
            submitionRepository.save(submition);
            //TODO: SEND REVIEWER ACCEPTED TO EDITOR
        } else {
            throw new ApiNotFoundException("Submition doesnt exist");
        }
    }

    public void rejectSubmitionReviewRequest(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = authenticationService.getCurrentAuthor();
            accessControlService.checkIfUserIsRequestedToReviewSubmition(reviewer, submition);
            submition.getRequestedReviewers().removeIf(requestedReviewer -> requestedReviewer.getId() == reviewer.getId());
            reviewer.getRequestedSubmitions().removeIf(submitionRequest -> submitionRequest.getId() == submition.getId());
            submitionRepository.save(submition);
            //TODO: SEND REVIEWER REJECT TO EDITOR
        } else {
            throw new ApiNotFoundException("Submition doesnt exist");
        }
    }

    public PageableResultsDTO<AuthorSubmitionDTO> getMyRequestedSubmitions(Pageable pageable) {
        Author reviewer = authenticationService.getCurrentAuthor();
        Page<Submition> submitionPage = new PageImpl<Submition>((List)reviewer.getRequestedSubmitions(), pageable, reviewer.getRequestedSubmitions().size());
        List<AuthorSubmitionDTO> authorSubmitionDTOS = submitionPage.getContent().stream().map(AuthorSubmitionDTO::new).collect(Collectors.toList());
        return new PageableResultsDTO(authorSubmitionDTOS, submitionPage.getTotalPages());
    }

    public PageableResultsDTO<AuthorSubmitionDTO> getMySubmitions(Pageable pageable) {
        Author reviewer = authenticationService.getCurrentAuthor();
        Page<Submition> submitionPage = new PageImpl<Submition>((List)reviewer.getSubmitions(), pageable, reviewer.getSubmitions().size());
        List<AuthorSubmitionDTO> authorSubmitionDTOS = submitionPage.getContent().stream().map(AuthorSubmitionDTO::new).collect(Collectors.toList());
        return new PageableResultsDTO(authorSubmitionDTOS, submitionPage.getTotalPages());
    }

    public void createReview(CreateReviewDTO createReviewDTO) {
        //TODO: WILL BE IMPLEMENTED AFTER XML SERVICE FOR REVIEW IS FIXED WITH CORRECT XML SCHEMA
        //TODO: SEND EMAIL TO NOTIFY EDITOR ABOUT REVIEW
    }
}
