package com.myFirstProject.myFirstProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class PromoCodeScheduler {
    private Logger logger = LoggerFactory.getLogger(PromoCodeScheduler.class);
    private PromoCodeService promoCodeService;

    @Autowired
    public void setPromoCodeService(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @Scheduled(cron = "0 01 12 * * *")
    @Transactional
    public void deleteExpiredPromoCode(){
        LocalDateTime time = LocalDateTime.now();
        logger.info(String.format("Start check promo codes at %s.", time));
        promoCodeService.deleteExpiredPromoCode(time);
    }
}
