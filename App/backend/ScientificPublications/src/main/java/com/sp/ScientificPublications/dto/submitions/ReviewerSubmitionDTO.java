package com.sp.ScientificPublications.dto.submitions;

import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.models.Submition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerSubmitionDTO {
    private Long submitionId;
    private String paperId;
    private String status;
    private String paperTitle;
    private UserDTO author;
    private Boolean reviewed;
    
    public ReviewerSubmitionDTO(Submition submition) {
    	submitionId = submition.getId();
        paperId = submition.getPaperId();
        paperTitle = submition.getPaperTitle();
        status = submition.getStatus().toString();
        author = new UserDTO(submition.getAuthor());
        reviewed = submition.getReviewersThatAddedReview().contains(author.getId());
    }
}
