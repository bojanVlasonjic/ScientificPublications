package com.sp.ScientificPublications.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
public class ApiNotValidException extends ApiException {

    private int lineNumber;
    private int column;

    public ApiNotValidException(String message, int lineNum, int column) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
        this.lineNumber = lineNum;
        this.column = column;
    }

    @Override
    public JsonNode getExceptionJson() {
        JsonNode node = super.getExceptionJson();
        ((ObjectNode)node).put("lineNumber", this.lineNumber);
        ((ObjectNode)node).put("column", this.column);
        return node;
    }
}
