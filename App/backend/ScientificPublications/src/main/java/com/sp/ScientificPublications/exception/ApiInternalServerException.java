package com.sp.ScientificPublications.exception;

import org.springframework.http.HttpStatus;

public class ApiInternalServerException extends ApiException {
    public ApiInternalServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
