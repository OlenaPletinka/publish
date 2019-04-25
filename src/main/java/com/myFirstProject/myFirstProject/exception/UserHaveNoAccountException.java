package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserHaveNoAccountException extends ServiceException{

    public UserHaveNoAccountException(String message) {
        super(message);
    }
}
