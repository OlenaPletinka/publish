package com.myFirstProject.myFirstProject.service;

import java.time.LocalDateTime;

public interface PromoCodeService {
    void deleteExpiredPromoCode(LocalDateTime time);
}
