package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.dto.PromoCodeReq;
import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {
    private Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private PromoCodeRepository promoCodeRepository;
    private ReqConverterService reqConverterService;

    @Autowired
    public void setPromoCodeRepository(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Autowired
    public void setReqConverterService(ReqConverterService reqConverterService) {
        this.reqConverterService = reqConverterService;
    }

    @Transactional
    @Override
    public void deleteExpiredPromoCode(LocalDateTime time) {
        List<PromoCode> promoCodes = promoCodeRepository.findByExpiredLessThan(time);
        logger.info(String.format("Find %d promo codes with expired date", promoCodes.size()));
        List promoCodesForDelete = promoCodes.stream()
                .filter(PromoCode::getValid)
                .collect(Collectors.toList());
        logger.info(String.format("Find %d promo codes for delete which was not use", promoCodesForDelete.size()));
        promoCodeRepository.deleteAll(promoCodesForDelete);
        logger.info(String.format("%d promo codes was delete", promoCodesForDelete.size()));
    }

    @Transactional
    @Override
    public String save(PromoCodeReq promoCodeReq) {
        PromoCode promoCode = reqConverterService.convert(promoCodeReq);
        promoCodeRepository.save(promoCode);

        return promoCode.getId();
    }
}
