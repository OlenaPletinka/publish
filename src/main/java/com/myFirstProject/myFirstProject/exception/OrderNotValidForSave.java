package com.myFirstProject.myFirstProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotValidForSave extends ServiceException {

    public OrderNotValidForSave(String message) {
        super(message);
    }
}
