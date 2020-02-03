package com.sp.ScientificPublications.dto.submitions;

import com.sp.ScientificPublications.models.Submition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorSubmitionDTO {
    private Long submitionId;
    private String paperId;
    private String paperTitle;
    private String coverLetterId;
    private String status;

    public AuthorSubmitionDTO(Submition submition) {
        submitionId = submition.getId();
        paperId = submition.getPaperId();
        paperTitle = submition.getPaperTitle();
        coverLetterId = submition.getCoverLetterId();
        status = submition.getStatus().toString();
    }
}
