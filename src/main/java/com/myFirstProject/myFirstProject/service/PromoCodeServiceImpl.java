package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {
    private Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    public void setPromoCodeRepository(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Transactional
    @Override
    public void deleteExpiredPromoCode(LocalDateTime time) {
        List<PromoCode> promoCodes = promoCodeRepository.findByExpiredDateLessThan(time);
        logger.info(String.format("Find %d promo codes with expired date", promoCodes.size()));
        promoCodeRepository.deleteAll(promoCodes);
        logger.info(String.format("%d promo codes was delete", promoCodes.size()));
    }
}
