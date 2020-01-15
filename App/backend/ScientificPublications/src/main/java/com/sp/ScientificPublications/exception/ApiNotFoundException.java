package com.sp.ScientificPublications.exception;


import org.springframework.http.HttpStatus;

public class ApiNotFoundException extends ApiException {


    public ApiNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
