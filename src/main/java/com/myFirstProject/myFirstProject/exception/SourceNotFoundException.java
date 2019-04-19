package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SourceNotFoundException extends ServiceException {

    public SourceNotFoundException(String message) {
        super(message);
    }
}
