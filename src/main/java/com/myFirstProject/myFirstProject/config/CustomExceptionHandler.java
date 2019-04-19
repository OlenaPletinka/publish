package com.myFirstProject.myFirstProject.config;

import com.myFirstProject.myFirstProject.exception.ArticleNotValidForCreateException;
import com.myFirstProject.myFirstProject.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    //унаслідувалися від цього класу щоб використати @ExceptionHandler

    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiErrorResponse> unknownException(Exception ex) {
        //ApiErrorResponse просто обєкт який ми створили щоб передати меседж
        if (ex instanceof ServiceException){
            logger.warn("Custom expected exception", ex);
            throw (ServiceException)ex;
        }
        logger.error("Unexpected exceptions", ex);
        return new ResponseEntity<>(new ApiErrorResponse("Service not available"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
