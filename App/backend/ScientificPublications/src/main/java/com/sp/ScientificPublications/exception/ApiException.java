package com.sp.ScientificPublications.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException{

    private HttpStatus status;
    private String message;
    private LocalDateTime timeStamp;

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

}
