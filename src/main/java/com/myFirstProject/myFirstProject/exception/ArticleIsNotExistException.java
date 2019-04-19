package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(BAD_REQUEST)
public class ArticleIsNotExistException extends ServiceException {
    public ArticleIsNotExistException(String message) {
        super(message);
    }
}
