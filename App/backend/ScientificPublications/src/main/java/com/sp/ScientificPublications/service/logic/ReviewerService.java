package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.PageableResultsDTO;
import com.sp.ScientificPublications.dto.reviews.CreateReviewDTO;

import com.sp.ScientificPublications.dto.reviews.PendingReviewDTO;
import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import com.sp.ScientificPublications.dto.submitions.AuthorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.EditorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.ReviewerSubmitionDTO;
import com.sp.ScientificPublications.exception.ApiInternalServerException;

import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Review;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.repository.ReviewRepository;
import com.sp.ScientificPublications.repository.SubmitionRepository;
import com.sp.ScientificPublications.service.PaperReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
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

    @Autowired
    private PaperReviewService paperReviewService;

    public void acceptSubmitionReviewRequest(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = authenticationService.getCurrentAuthor();
            accessControlService.checkIfUserCanAcceptDeclineReviewRequest(reviewer, submition);
            submition.getRequestedReviewers().removeIf(requestedReviewer -> requestedReviewer.getId() == reviewer.getId());
            reviewer.getRequestedSubmitions().removeIf(submitionRequest -> submitionRequest.getId() == submition.getId());
            submition.getReviewers().add(reviewer);
            reviewer.getReviewedSubmitions().add(submition);

            submitionRepository.save(submition);
        } else {
            throw new ApiNotFoundException("Submition doesnt exist");
        }
    }

    public void rejectSubmitionReviewRequest(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = authenticationService.getCurrentAuthor();
            accessControlService.checkIfUserCanAcceptDeclineReviewRequest(reviewer, submition);
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

    public List<ReviewerSubmitionDTO> getMySubmitions(Pageable pageable) {
        Author reviewer = authenticationService.getCurrentAuthor();
        return reviewer.getReviewedSubmitions()
                .stream()
                .map(ReviewerSubmitionDTO::new)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsForPaper(String paperId) {
        return reviewRepository
                .findAllBySubmitionPaperId(paperId)
                .stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());
    }

}
