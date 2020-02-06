package com.sp.ScientificPublications.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchResultDTO {

    private String content;
    private String title;
    private String author;
    private Integer rank;

    public SearchResultDTO() {

    }
}
