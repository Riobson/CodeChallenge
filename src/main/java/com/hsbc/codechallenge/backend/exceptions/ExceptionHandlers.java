package com.hsbc.codechallenge.backend.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleApplicationException(
            ApplicationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getUuid());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleTransactionException(TransactionSystemException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause().getCause().getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "ERROR OCCURRED");
        return new ResponseEntity<Object>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
