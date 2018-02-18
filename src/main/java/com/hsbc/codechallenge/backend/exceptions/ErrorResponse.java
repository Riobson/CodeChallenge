package com.hsbc.codechallenge.backend.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Setter
@Getter
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private UUID uuid;


    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, UUID uuid) {
        this.status = status;
        this.message = message;
        this.uuid = uuid;
    }
}
