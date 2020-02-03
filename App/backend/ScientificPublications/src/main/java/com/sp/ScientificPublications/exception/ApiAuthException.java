package com.sp.ScientificPublications.exception;

import org.springframework.http.HttpStatus;

public class ApiAuthException extends ApiException {
    public ApiAuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
