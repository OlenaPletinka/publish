package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PromoCodeReq;

import java.time.LocalDateTime;

public interface PromoCodeService {
    void deleteExpiredPromoCode(LocalDateTime time);

    String save(PromoCodeReq promoCodeReq);
}
