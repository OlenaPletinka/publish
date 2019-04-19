package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.PaymentSystemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class PaymentReq {
    private Long userId;
    private BigDecimal sum;
    private CurrencyEntity currencyEntity;
    private PaymentSystemEntity paymentSystemEntity;
}
