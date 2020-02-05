package com.sp.ScientificPublications.dto.submitions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class CreateSubmitionDTO {

    @NotEmpty(message = "Provide paper content.")
    private String paperContent;

    private String coverLetterContent;
}
