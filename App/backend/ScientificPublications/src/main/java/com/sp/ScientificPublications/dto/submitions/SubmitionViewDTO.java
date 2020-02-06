package com.sp.ScientificPublications.dto.submitions;

import com.sp.ScientificPublications.dto.AuthorDTO;
import com.sp.ScientificPublications.models.Submition;
import com.sp.ScientificPublications.models.scientific_paper.ScientificPaper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class SubmitionViewDTO {

    private Long submitionId;
    private String paperId;
    private String paperTitle;
    private List<AuthorDTO> authors;
    private String status;

    public SubmitionViewDTO(Submition submition, ScientificPaper paper) {
        this.submitionId = submition.getId();
        this.paperId = submition.getPaperId();
        this.paperTitle = paper.getHeader().getTitle();

        this.authors = paper
                .getHeader()
                .getAuthors()
                .getAuthor()
                .stream()
                .map(author -> new AuthorDTO(author))
                .collect(Collectors.toList());

    }
}
