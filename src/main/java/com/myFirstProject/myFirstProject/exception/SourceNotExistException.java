package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SourceNotExistException extends ServiceException {

    public SourceNotExistException(String message) {
        super(message);
    }
}
