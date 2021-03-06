package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotFoundException extends ServiceException {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
