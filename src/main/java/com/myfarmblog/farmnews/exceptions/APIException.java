package com.myfarmblog.farmnews.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException{

    private final String message;

    public APIException( String message) {
        super(message);
        this.message = message;
    }

}
