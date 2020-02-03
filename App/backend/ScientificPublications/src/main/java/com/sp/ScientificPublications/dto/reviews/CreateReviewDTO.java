package com.sp.ScientificPublications.dto.reviews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewDTO {

    @NotNull(message = "Provide submition id.")
    private Long submitionId;

    @NotEmpty(message = "Provide review content.")
    private String reviewContent;
}
