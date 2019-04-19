package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorisedException extends ServiceException {

    public UserNotAuthorisedException(String message) {
        super(message);
    }
}
