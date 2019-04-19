package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Year;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughManyOnAccountException extends ServiceException {

    public NotEnoughManyOnAccountException(String message) {
        super(message);
    }
}
