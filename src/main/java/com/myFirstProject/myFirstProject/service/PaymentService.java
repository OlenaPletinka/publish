package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentResp;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService  {
    List<PaymentResp> getAllPaymentByUserId(Pageable pageable, Long userId);

    List<PaymentResp> findAllBySum(BigDecimal sum, Pageable pageable);
}
