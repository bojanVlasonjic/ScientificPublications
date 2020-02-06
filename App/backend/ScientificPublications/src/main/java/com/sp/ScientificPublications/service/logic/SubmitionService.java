package com.sp.ScientificPublications.service.logic;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.dto.PageableResultsDTO;
import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import com.sp.ScientificPublications.dto.submitions.AuthorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.CreateSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.EditorSubmitionDTO;
import com.sp.ScientificPublications.dto.submitions.SubmitionViewDTO;
import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import com.sp.ScientificPublications.repository.AuthorRepository;
import com.sp.ScientificPublications.repository.SubmitionRepository;
import com.sp.ScientificPublications.repository.rdf.RdfRepository;
import com.sp.ScientificPublications.repository.rdf.SparqlUtil;
import com.sp.ScientificPublications.service.CoverLetterService;
import com.sp.ScientificPublications.service.ScientificPaperService;
import com.sp.ScientificPublications.service.UtilityService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubmitionService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SubmitionRepository submitionRepository;

    @Autowired
    private CoverLetterService coverLetterService;

    @Autowired
    private ScientificPaperService scientificPaperService;

    @Autowired
    private AccessControlService accessControlService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RdfRepository rdfRepository;

    @Autowired
    private UtilityService utilityService;


    public List<UserDTO> getAllReviewersForSubmition(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            List<UserDTO> results = new ArrayList<>();

            submition.getRequestedReviewers().stream().forEach(reviewer -> {
                UserDTO userDTO = new UserDTO(reviewer);
                userDTO.setReviewerStatus("REQUESTED");
                results.add(userDTO);
            });

            submition.getReviewers().stream().forEach(reviewer -> {
                UserDTO userDTO = new UserDTO(reviewer);
                userDTO.setReviewerStatus("ACCEPTED");
                results.add(userDTO);
            });

            return results;
        } else {
            throw new ApiNotFoundException("Submition doesn't exist.");
        }
    }
    
    public List<UserDTO> getRequestedReviewers(String paperId) {
    	Submition submition = submitionRepository.findByPaperId(paperId);
    	List<UserDTO> users = new ArrayList<>();
    	for (Author author : submition.getRequestedReviewers()) {
    		users.add(new UserDTO(author));
    	}
    	return users;
    }

    public PageableResultsDTO<EditorSubmitionDTO> getSubmitions(Pageable pageable) {
        authenticationService.getCurrentEditor();
        Page<Submition> submitionPage = submitionRepository.findAll(pageable);
        List<EditorSubmitionDTO> editorsSubmitionDTOS = submitionPage.get().map(EditorSubmitionDTO::new).collect(Collectors.toList());
        return new PageableResultsDTO<>(editorsSubmitionDTOS, submitionPage.getTotalPages());
    }

    public List<SubmitionViewDTO> getPublishedSubmitions() {

        return submitionRepository
                .findAllByStatus(SubmitionStatus.PUBLISHED)
                .stream()
                .map(subm ->
                        new SubmitionViewDTO(subm,
                                scientificPaperService.retrieveScientificPaperAsObject(subm.getPaperId())))
                .collect(Collectors.toList());

    }

    public AuthorSubmitionDTO createSubmition(CreateSubmitionDTO createSubmitionDTO) {
        Author author = authenticationService.getCurrentAuthor();
        DocumentDTO paper = new DocumentDTO(null, createSubmitionDTO.getPaperContent());
        paper = scientificPaperService.storeScientificPaperAsDocument(paper);
        scientificPaperService.generateHtml(paper.getDocumentId());
        scientificPaperService.generatePdf(paper.getDocumentId());

        DocumentDTO coverLetter = new DocumentDTO();

        // if the cover letter was provided
        if(createSubmitionDTO.getCoverLetterContent() != null && !createSubmitionDTO.getCoverLetterContent().equals("")) {
            coverLetter.setDocumentContent(createSubmitionDTO.getCoverLetterContent());
            coverLetter = coverLetterService.storeCoverLetterAsDocument(coverLetter);
            coverLetterService.generateHtml(coverLetter.getDocumentId());
            coverLetterService.generatePdf(coverLetter.getDocumentId());
        } else {
            coverLetter.setDocumentId(null);
        }

        String title = scientificPaperService.retrieveScientificPaperAsObject(paper.getDocumentId()).getHeader().getTitle();

        Submition submition = new Submition(paper.getDocumentId(), title, coverLetter.getDocumentId(), SubmitionStatus.NEW);
        author.getSubmitions().add(submition);
        submition.setAuthor(author);
        submition = submitionRepository.save(submition);
        generateAndSaveMetadata(submition);
        return new AuthorSubmitionDTO(submitionRepository.save(submition));
    }

    public void generateAndSaveMetadata(Submition submition) {
        Model model = ModelFactory.createDefaultModel();

        // DATE CREATED
        model.createResource(SparqlUtil.SUBJECT_URI + "/submition/" + submition.getPaperId())
                .addProperty(new PropertyImpl(SparqlUtil.PROPERTY_URI + "/dateCreated"),
                        utilityService.formatDate(submition.getDateCreated()));

        // STATUS
        model.createResource(SparqlUtil.SUBJECT_URI + "/submition/" + submition.getPaperId())
                .addProperty(new PropertyImpl(SparqlUtil.PROPERTY_URI + "/status"), submition.getStatus().toString());

        rdfRepository.saveModelToDb(model);
    }



    public AuthorSubmitionDTO createSubmitionFile(MultipartFile[] files) {
        Author author = authenticationService.getCurrentAuthor();
        DocumentDTO paper = scientificPaperService.uploadScientificPaperXMLFile(files[0]);
        String title = scientificPaperService
                .retrieveScientificPaperAsObject(paper.getDocumentId())
                .getHeader()
                .getTitle();

        DocumentDTO coverLetter = new DocumentDTO();
        // if the cover letter was provided
        if(files.length > 1) {
            coverLetter = coverLetterService.uploadCoverLetterXMLFile(files[1]);
        } else {
            coverLetter.setDocumentId(null);
        }

        Submition submition = new Submition(paper.getDocumentId(), title, coverLetter.getDocumentId(), SubmitionStatus.NEW);
        author.getSubmitions().add(submition);
        submition.setAuthor(author);
        generateAndSaveMetadata(submition);
        return new AuthorSubmitionDTO(submitionRepository.save(submition));
    }

    public AuthorSubmitionDTO cancelSubmition(Long id) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {
            Author user = authenticationService.getCurrentAuthor();
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfUserOwnsSubmition(user, submition);
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.CANCELED);
            submition.setStatus(SubmitionStatus.CANCELED);
            return new AuthorSubmitionDTO(submitionRepository.save(submition));
        } else {
            throw new ApiNotFoundException("Submition doesn't exist.");
        }
    }

    public List<AuthorSubmitionDTO> mySubmitions() {
        Author author = authenticationService.getCurrentAuthor();
        return author.getSubmitions().stream().map(AuthorSubmitionDTO::new).collect(Collectors.toList());
    }

    public void approveSubmition(Long id) {
        authenticationService.getCurrentEditor();

        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.IN_REVIEW_PROCESS);
            submition.setStatus(SubmitionStatus.IN_REVIEW_PROCESS);
            submitionRepository.save(submition);
            //TODO: SEND APPROVED EMAIL TO AUTHOR
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void rejectSubmition(Long id) {
        authenticationService.getCurrentEditor();

        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.REJECTED);
            submition.setStatus(SubmitionStatus.REJECTED);
            //TODO: SEND REJECTION EMAIL
            //TODO: NOTIFY REVIEWES THAT SUBMITION IS REJECTED
            cleanSubmitionFromReviewers(submition);
            submitionRepository.save(submition);
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void cleanSubmitionFromReviewers(Submition submition) {
        // remove this submition from all reviewers
        submition.getReviewers().stream().forEach(reviewer -> reviewer.getSubmitions().removeIf(s -> s.getId() == submition.getId()));

        // remove this submition from all requested reviewers
        submition.getRequestedReviewers().stream().forEach(requestReviewer -> requestReviewer.getRequestedSubmitions().removeIf(rs -> rs.getId() == submition.getId()));

        // remove reviewers from submitions
        submition.setRequestedReviewers(new HashSet<>());
        submition.setReviewers(new HashSet<>());
    }


    public void requestRevision(Long id) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.REVISIONS_REQUESTED);
            submition.setStatus(SubmitionStatus.REVISIONS_REQUESTED);
            submitionRepository.save(submition);
            //TODO: SEND REVISION REQUESTED EMAIL
            //TODO: NOTIFY REVIEWERS THAT REVISION IS REQUESTED
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void reviseSubmition(Long id, CreateSubmitionDTO revisedSubmitonDTO) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.REVISED);
            submition.setStatus(SubmitionStatus.REVISED);
            submition.setDateRevised(new Date());
            //TODO: SAVE REVISED DOCUMENT TO XML DATABASE
            //TODO: SEND REVISED NOTIFICATION EMAIL TO REVIEWERS AND EDITOR
            submition = submitionRepository.save(submition);

            Model model = ModelFactory.createDefaultModel();
            model.createResource(SparqlUtil.SUBJECT_URI + "/submition/" + submition.getPaperId())
                    .addProperty(new PropertyImpl(SparqlUtil.PROPERTY_URI + "/dateRevised"), submition.getDateRevised().toString());
            rdfRepository.saveModelToDb(model);
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void publishSubmition(Long id) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(id);
        if (optionalSubmition.isPresent()) {

            // get submition and check if can be published
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.PUBLISHED);

            // save published submition
            submition.setStatus(SubmitionStatus.PUBLISHED);
            submition.setDatePublished(new Date());
            submition = submitionRepository.save(submition);

            // generate metadata and save to RDF
            Model model = ModelFactory.createDefaultModel();
            model.createResource(SparqlUtil.SUBJECT_URI + "/submition/" + submition.getPaperId())
                    .addProperty(new PropertyImpl(SparqlUtil.PROPERTY_URI + "/datePublished"),
                            utilityService.formatDate(submition.getDatePublished()));
            rdfRepository.saveModelToDb(model);

            // send email
            //TODO: SEND PUBLISHED NOTIFICATION EMAIL TO AUTHOR
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void requestReview(Long submitionId, Long reviewerId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        Optional<Author> optionalReviewer = authorRepository.findById(reviewerId);

        if (optionalSubmition.isPresent() && optionalReviewer.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = optionalReviewer.get();
            accessControlService.checkIfRequestReviewIsPossible(submition);
            accessControlService.checkIfUserIsRequestedToReviewSubmition(reviewer, submition);
            accessControlService.checkIfUserIsNotReviewerForSubmition(reviewer, submition);
            submition.getRequestedReviewers().add(reviewer);
            reviewer.getRequestedSubmitions().add(submition);
            submitionRepository.save(submition);
            //TODO: SEND REQUESTED REVIEW EMAIL TO REVIEWER
        } else {
            throw new ApiNotFoundException("Submition and/or reviewer doesnt exist.");
        }
    }

    public void cancelRequestReview(Long submitionId, Long reviewerId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        Optional<Author> optionalReviewer = authorRepository.findById(reviewerId);

        if (optionalSubmition.isPresent() && optionalReviewer.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = optionalReviewer.get();

            submition.getRequestedReviewers().removeIf(r -> r.getId() == reviewerId);
            reviewer.getRequestedSubmitions().removeIf(s -> s.getId() == submitionId);
            submitionRepository.save(submition);
            //TODO: SEND REQUESTED REVIEW EMAIL TO REVIEWER
        } else {
            throw new ApiNotFoundException("Submition and/or reviewer doesnt exist.");
        }
    }

    public List<ReviewDTO> getReviewsForSubmitionEditor(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            List<ReviewDTO> reviewDTOS = submition.getReviews().stream().map(ReviewDTO::new).collect(Collectors.toList());
            return reviewDTOS;
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }

    public void setSubmitionStatusInReviewProcess(Long submitionId) {
        Optional<Submition> optionalSubmition = submitionRepository.findById(submitionId);
        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            accessControlService.checkIfAllReviewersAcceptedRequests(submition);
            accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.IN_REVIEW_PROCESS);
            submition.setStatus(SubmitionStatus.IN_REVIEW_PROCESS);
            submition.setReviewersThatAddedReview(new HashSet<>());
            submitionRepository.save(submition);
        } else {
            throw new ApiNotFoundException("Submition doesnt exist.");
        }
    }
}
