package com.sp.ScientificPublications.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiException extends RuntimeException{

    private HttpStatus status;
    private String message;
    private LocalDateTime timeStamp;

    public ApiException() {

    }

    public ApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }


    public JsonNode getExceptionJson() {

        ObjectNode exceptionNode = new ObjectMapper().createObjectNode();
        exceptionNode.put("message", this.getMessage());
        exceptionNode.put("status", this.getStatus().toString());
        exceptionNode.put("timestamp", this.getTimeStamp().toString());

        return exceptionNode;

    }


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
