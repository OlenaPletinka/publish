package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryNotValidForUpdate extends ServiceException {
    public CategoryNotValidForUpdate(String message) {
        super(message);
    }
}
