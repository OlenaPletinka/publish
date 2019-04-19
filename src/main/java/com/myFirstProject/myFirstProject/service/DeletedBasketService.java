package com.myFirstProject.myFirstProject.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface DeletedBasketService {
    BigDecimal getAmountOfSpentMoneyFromDeletedBaskets (Long userId);
    BigDecimal revenueFromDeletedBaskets();
}
