package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.BasketReq;

import java.math.BigDecimal;

public interface BasketService {
    Long saveBasket(BasketReq basketReq);
    BigDecimal getAmountOfSpentMoneyFromBaskets (Long userId);
    BigDecimal revenueFromBaskets();

}
