package com.hsbc.codechallenge.backend.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private HttpStatus status;
    private String message;
    private UUID uuid;


    public ApplicationException(String message) {
        this.message = message;
        uuid = UUID.randomUUID();
    }

    public ApplicationException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        uuid = UUID.randomUUID();
    }
}