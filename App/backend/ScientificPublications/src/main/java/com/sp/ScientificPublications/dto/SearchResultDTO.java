package com.sp.ScientificPublications.dto;

import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.SubmitionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDTO {

    private String paperId;
    private String title;
    private String author;
    private Long rank;
    private SubmitionStatus status;
}
