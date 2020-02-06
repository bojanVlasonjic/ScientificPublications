package com.sp.ScientificPublications.dto;

import com.sp.ScientificPublications.models.common.Author;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthorDTO {

    private String firstName;
    private String lastName;

    public AuthorDTO(Author author) {
        this.firstName  = author.getFirstName();
        this.lastName = author.getLastName();
    }
}
