package com.sp.ScientificPublications.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiExceptionHandler  {

    @ResponseBody
    @ExceptionHandler(value=ApiException.class)
    protected ResponseEntity<Object> handleApiException(ApiException ex) {
        return new ResponseEntity<>(ex.getExceptionJson(), ex.getStatus());
    }

}
