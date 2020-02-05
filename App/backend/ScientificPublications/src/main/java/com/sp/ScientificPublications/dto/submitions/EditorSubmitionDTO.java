package com.sp.ScientificPublications.dto.submitions;

import com.sp.ScientificPublications.dto.reviews.ReviewDTO;
import com.sp.ScientificPublications.dto.UserDTO;
import com.sp.ScientificPublications.models.Submition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditorSubmitionDTO {
    private String paperId;
    private String paperTitle;
    private String coverLetterId;
    private String status;
    private UserDTO author;
    private List<UserDTO> reviewers;
    private List<ReviewDTO> reviews;

    public EditorSubmitionDTO(Submition submition) {
        paperId = submition.getPaperId();
        paperTitle = submition.getPaperTitle();
        coverLetterId = submition.getCoverLetterId();
        status = submition.getStatus().toString();
        author = new UserDTO(submition.getAuthor());
        reviewers = submition.getReviewers().stream().map(UserDTO::new).collect(Collectors.toList());
        reviews = submition.getReviews().stream().map(ReviewDTO::new).collect(Collectors.toList());
    }
}

