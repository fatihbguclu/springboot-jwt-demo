package com.jwt.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{

    private final String message;
    private final HttpStatus status;

    public CustomException(String message,  HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
