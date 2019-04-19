package com.myFirstProject.myFirstProject.exception;

public class NotValidUserIdException extends ServiceException {

    public NotValidUserIdException(String message) {
        super(message);
    }
}
