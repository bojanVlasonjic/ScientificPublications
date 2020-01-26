package com.sp.ScientificPublications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentDTO {

    private String documentId;
    private String documentContent;

}
