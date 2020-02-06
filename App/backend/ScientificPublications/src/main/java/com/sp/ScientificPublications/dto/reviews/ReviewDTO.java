package com.sp.ScientificPublications.dto.reviews;

import com.sp.ScientificPublications.models.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private String reviewerName;
    private String paperTitle;
    private String reviewId;

    public ReviewDTO(Review review) {
        id = review.getId();
        reviewerName = review.getReviewer().getFirstname() + " " + review.getReviewer().getLastname();
        reviewId = review.getReviewId();
        paperTitle = review.getSubmition().getPaperTitle();
    }

}
