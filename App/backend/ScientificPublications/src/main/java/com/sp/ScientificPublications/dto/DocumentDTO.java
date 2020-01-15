package com.sp.ScientificPublications.dto;

public class DocumentDTO {

    private String documentName; // just in case we need it
    private String documentContent;

    public DocumentDTO() {

    }


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }
}
