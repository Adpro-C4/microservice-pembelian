package com.adpro.pembelian.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseHandlerTest {

    @Test
    void testGenerateResponse() {

        new ResponseHandler();
        // Test data
        String message = "Success";
        HttpStatus status = HttpStatus.OK;
        Object responseData = new Object();

        // Generate response entity
        ResponseEntity<Object> responseEntity = ResponseHandler.generateResponse(message, status, responseData);

        // Extract response data
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        // Check response data
        assertEquals(message, responseBody.get("message"));
        assertEquals(status.value(), responseBody.get("status"));
        assertEquals(responseData, responseBody.get("data"));
    }
}

