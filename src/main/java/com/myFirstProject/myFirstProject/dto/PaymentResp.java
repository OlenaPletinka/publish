package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.PaymentSystemEntity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResp {
    private Long id;
    private PaymentSystemEntity paymentSystemEntity;
    private LocalDateTime timeOfPayment;
    private BigDecimal sum;
    private CurrencyEntity currencyEntity;
    private BigDecimal rate;
}
