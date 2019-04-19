package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.exception.PaymentReqNotValidForApdateException;
import com.myFirstProject.myFirstProject.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/account/")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAccount(@RequestBody PaymentReq paymentReq) {
        validate(paymentReq);
        accountService.updateAccount(paymentReq);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAmountOfSpentMoney(@PathVariable Long userId) throws JsonProcessingException {
        BigDecimal amountOfSpentMoney = accountService.getAmountOfSpentMoney(userId);

        return ResponseEntity.ok(objectMapper.writeValueAsString(amountOfSpentMoney));
    }

    @RequestMapping(path = "revenue/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity revenue() throws JsonProcessingException {
        BigDecimal totalRevenue = accountService.getTotalRevenue();

        return ResponseEntity.ok(objectMapper.writeValueAsString(totalRevenue));
    }

    private void validate(PaymentReq paymentReq) {
        if (paymentReq.getSum().compareTo(BigDecimal.ZERO) < 1 || paymentReq.getUserId() == null||paymentReq.getCurrencyEntity()==null||paymentReq.getPaymentSystemEntity()==null) {
            throw new PaymentReqNotValidForApdateException("Payment is not valid for account update");
        }
    }
}
