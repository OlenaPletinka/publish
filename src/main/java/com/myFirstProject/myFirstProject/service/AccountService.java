package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.model.*;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void updateAccount(PaymentReq paymentReq);
    void payForArticles(BigDecimal tacks, Long id, PromoCode promoCode);
    BigDecimal getAmountOfSpentMoney(Long userId);
    BigDecimal getTotalRevenue();
    BigDecimal currencyConverter(BigDecimal sum, CurrencyEntity currencyEntity);
    BigDecimal calculateTaxPerMonth(List<Payment> paymentPerMonth);

}
