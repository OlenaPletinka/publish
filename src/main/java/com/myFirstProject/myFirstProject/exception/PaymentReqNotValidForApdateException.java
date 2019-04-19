package com.myFirstProject.myFirstProject.exception;

import com.myFirstProject.myFirstProject.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentReqNotValidForApdateException extends ServiceException{

    public PaymentReqNotValidForApdateException(String message) {
        super(message);
    }
}
