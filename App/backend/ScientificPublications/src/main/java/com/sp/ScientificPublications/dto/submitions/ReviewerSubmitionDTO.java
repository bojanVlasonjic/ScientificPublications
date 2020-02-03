package com.sp.ScientificPublications.dto.submitions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerSubmitionDTO {
    private String submitionId;
    private String paperId;
    private String status;
}
