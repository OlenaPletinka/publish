package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.dto.PromoCodeReq;
import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PromoCodeServiceImplTest {
    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private ReqConverterService reqConverterService;

    @Test
    public void deleteExpiredPromoCode() {
//        Given
        LocalDateTime time = LocalDateTime.now();
        PromoCodeServiceImpl promoCodeService = new PromoCodeServiceImpl();
        promoCodeService.setPromoCodeRepository(promoCodeRepository);
        Mockito.when(promoCodeRepository.findByExpiredLessThan(time)).thenReturn(buildListOfPromoCode());
//        When
        promoCodeService.deleteExpiredPromoCode(time);
//        Then
        Mockito.verify(promoCodeRepository).findByExpiredLessThan(time);
        Mockito.verify(promoCodeRepository).deleteAll(getValidPromoCodes());
    }

    private List<PromoCode> buildListOfPromoCode() {
        List<PromoCode> promoCodes = getValidPromoCodes();
        promoCodes.add(getValidPromoCode(false));

        return promoCodes;
    }

    private List<PromoCode> getValidPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodes.add(getValidPromoCode(true));
        promoCodes.add(getValidPromoCode(true));

        return promoCodes;
    }

    private PromoCode getValidPromoCode(boolean b) {
        PromoCode promoCode = getPromoCode();
        promoCode.setValid(b);
        return promoCode;
    }

    private PromoCode getPromoCode() {
        PromoCode promoCode = new PromoCode();
        promoCode.setId("PROMO_10");
        promoCode.setValue(BigDecimal.TEN);

        return promoCode;
    }

    @Test
    public void save() {
//        Given
        PromoCodeServiceImpl promoCodeService = new PromoCodeServiceImpl();
        PromoCodeReq promoCodeReq = new PromoCodeReq();
        promoCodeService.setReqConverterService(reqConverterService);
        Mockito.when(reqConverterService.convert(promoCodeReq)).thenReturn(getPromoCode());
        promoCodeService.setPromoCodeRepository(promoCodeRepository);
//        When
        String actualResult = promoCodeService.save(promoCodeReq);
//        Then
        Assert.assertEquals("PROMO_10", actualResult);
        Mockito.verify(promoCodeRepository).save(getPromoCode());
    }
}