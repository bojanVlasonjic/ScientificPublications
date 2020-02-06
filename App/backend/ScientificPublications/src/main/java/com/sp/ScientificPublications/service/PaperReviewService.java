package com.sp.ScientificPublications.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.sp.ScientificPublications.dto.reviews.CreateReviewDTO;
import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import com.sp.ScientificPublications.exception.ApiNotFoundException;
import com.sp.ScientificPublications.models.Author;
import com.sp.ScientificPublications.models.Review;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import com.sp.ScientificPublications.repository.ReviewRepository;
import com.sp.ScientificPublications.repository.SubmitionRepository;
import com.sp.ScientificPublications.repository.rdf.RdfRepository;
import com.sp.ScientificPublications.service.logic.AccessControlService;
import com.sp.ScientificPublications.service.logic.AuthenticationService;
import com.sp.ScientificPublications.utility.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import com.sp.ScientificPublications.repository.exist.ExistDocumentRepository;
import com.sp.ScientificPublications.repository.exist.ExistJaxbRepository;

@Service
public class PaperReviewService {
	
	@Autowired
    DomParserService domParserSvc;

    @Autowired
    XmlTransformerService xmlTransformSvc;

    @Autowired
    ExistDocumentRepository existDocumentRepo;

    @Autowired
    ExistJaxbRepository existJaxbRepo;

    @Autowired
    SubmitionRepository submitionRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccessControlService accessControlService;

    @Autowired
    ReviewRepository reviewRepository;

    private static final String schemaPath = "src/main/resources/data/xsd_schema/paper-review.xsd";
    private static final String xslFilePath = "src/main/resources/data/xsl_fo/paper-review-fo.xsl";
    private static final String templatePath = "src/main/resources/templates/paper-review-template.xml";
    private static final String xsltFilePath = "src/main/resources/data/xslt/paper-review-xslt.xsl";
    
    private static final String PDF_DIRECTORY_PATH = "src/main/resources/output/pdf/paper-review/";

    private static final String collectionId = "/db/scientific-publication/paper-reviews";
    private static final String modelPackage = "com.sp.ScientificPublications.models.paper_review";
    
    // ============================== VIEW ==============================
    public ResponseEntity<byte[]> viewPaperReview(String id) throws URISyntaxException, IOException {
    	
    	String filename = PDF_DIRECTORY_PATH + id + ".pdf";
    	File pdf = new File(filename);
    	if (!pdf.exists()) throw new ApiBadRequestException("No such file");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.parseMediaType("application/pdf"));
    	headers.add("content-disposition", "inline;filename=" + filename);
    	headers.setCacheControl("must-revalidate, post-check=0 pre-check=0");
    	
    	byte[] file = xmlTransformSvc.loadFile(filename);
    	
    	return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
    }
    // ============================================================

	public boolean validatePaperReviewXMLFile(MultipartFile file) {
    	return this.validatePaperReview(domParserSvc.readMultipartXMLFile(file));
    }
    
    public DocumentDTO uploadPaperReviewXMLFile(MultipartFile file) {
    	String xmlContent = domParserSvc.readMultipartXMLFile(file);
    	DocumentDTO document = new DocumentDTO();

    	document.setDocumentContent(xmlContent);
    	document = this.storePaperReviewAsDocument(document);

    	this.generatePdf(document.getDocumentId());
    	this.generateHtml(document.getDocumentId());

    	return document;
    }
    
    public String generatePdf(String documentId) {
    	DocumentDTO retrievedDTO = this.retrievePaperReviewAsDocument(documentId);
    	retrievedDTO.setDocumentId("paper-review/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generatePdfFromXml(retrievedDTO, xslFilePath);
    }
    
    public String generateHtml(String documentId) {
    	DocumentDTO retrievedDTO = this.retrievePaperReviewAsDocument(documentId);
    	retrievedDTO.setDocumentId("paper-review/" + retrievedDTO.getDocumentId());
        return xmlTransformSvc.generateHtmlFromXml(retrievedDTO, xsltFilePath);
    }
    
    public DocumentDTO getTemplate() {

        DocumentDTO templateDTO = new DocumentDTO();
        try {
            templateDTO.setDocumentContent(FileUtil.readFile(templatePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiBadRequestException("Failed to generate paper review template");
        }

        return templateDTO;
    }
    
    public boolean validatePaperReview(String documentContent) {
        return domParserSvc.validateXmlDocument(documentContent, schemaPath);
    }
	
    public DocumentDTO retrievePaperReviewAsDocument(String documentId) {

        XMLResource resource;
        DocumentDTO document;

        try {
            resource = existDocumentRepo.retrieveXmlFile(collectionId, documentId);
            document = new DocumentDTO(resource.getId(), resource.getContent().toString());
        } catch (XMLDBException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to retrieve the cover document review");
        }

        return document;

    }
    
    public DocumentDTO storePaperReviewAsDocument(DocumentDTO documentDTO) {

        if(!validatePaperReview(documentDTO.getDocumentContent())) {
            throw new ApiBadRequestException("The paper review is not in a valid format");
        }

        XMLResource resource;

        try {
            resource = existDocumentRepo.storeXmlFile(collectionId, documentDTO);
            documentDTO.setDocumentId(resource.getDocumentId());
        } catch (XMLDBException ex) {
            ex.printStackTrace();
            throw new ApiBadRequestException("Failed to store xml file");
        }

        return documentDTO;
    }

    @Autowired
    private RdfRepository rdfRepository;

    public void saveToRdf(String id) {
        DocumentDTO documentDTO = retrievePaperReviewAsDocument(id);
        String xml = documentDTO.getDocumentContent();
        rdfRepository.saveMetadataFromXml(xml);
    }

    public ReviewDTO createReview(CreateReviewDTO createReviewDTO) {
        //TODO: SEND EMAIL TO NOTIFY EDITOR ABOUT REVIEW

        Optional<Submition> optionalSubmition = submitionRepository.findById(createReviewDTO.getSubmitionId());

        if (optionalSubmition.isPresent()) {
            Submition submition = optionalSubmition.get();
            Author reviewer = authenticationService.getCurrentAuthor();

            accessControlService.checkIfUserIsReviewerForSubmition(reviewer, submition);

            if (submition.getStatus() == SubmitionStatus.IN_REVIEW_PROCESS) {
                DocumentDTO documentDTO = new DocumentDTO(null, createReviewDTO.getReviewContent());
                DocumentDTO paperReview = storePaperReviewAsDocument(documentDTO);

                Review review = new Review(paperReview.getDocumentId(), reviewer, submition);
                reviewer.getReviews().add(review);
                submition.getReviews().add(review);

                // if all reviewers added their review submition changes its state to REVIEWED
                if (submition.getRequestedReviewers().size() == 0 && submition.getReviews().size() == submition.getReviewers().size()) {
                    accessControlService.checkIfTransitionIsPossible(submition.getStatus(), SubmitionStatus.REVIEWED);
                    submition.setStatus(SubmitionStatus.REVIEWED);
                }

                review = reviewRepository.save(review);
                return new ReviewDTO(review);
                // TODO: SEND EMAIL TO EDITOR
            } else {
                throw new ApiBadRequestException("Submition is not in review process state.");
            }
        } else {
            throw new ApiNotFoundException("Submition not found.");
        }
    }
}
