package com.user.demo.controller;

import com.user.demo.exceptions.CustomException;
import com.user.demo.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.Collections;

public abstract class BaseController {
    protected ResponseEntity<?> handleCustomException(CustomException e) {
        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail(
                Instant.now(), e.getHttpStatus().value(), e.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    protected ResponseEntity<?> handleGenericException(Exception e) {
        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail(
                Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor"
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

