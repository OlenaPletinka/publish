package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.model.Account;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.CurrencyRate;
import com.myFirstProject.myFirstProject.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void updateAccount(PaymentReq paymentReq);
    void payForArticles(BigDecimal tacks, Long id);
    BigDecimal getAmountOfSpentMoney(Long userId);
    BigDecimal getTotalRevenue();
    BigDecimal currencyConverter(BigDecimal sum, CurrencyEntity currencyEntity);
    BigDecimal calculateTaxPerMonth(List<Payment> paymentPerMonth);

}
