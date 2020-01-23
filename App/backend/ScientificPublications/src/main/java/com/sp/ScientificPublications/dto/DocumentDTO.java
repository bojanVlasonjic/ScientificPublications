package com.sp.ScientificPublications.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Setter
public class DocumentDTO {

    private String documentId; // just in case we need it
    private String documentContent;

}
