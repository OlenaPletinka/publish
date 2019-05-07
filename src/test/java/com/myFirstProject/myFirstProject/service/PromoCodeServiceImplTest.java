package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.converter.ReqConverterServiceImpl;
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

import static org.junit.Assert.*;

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
        Mockito.when(promoCodeRepository.findByExpiredDateLessThan(time)).thenReturn(buildListOfPromoCode());
//        When
        promoCodeService.deleteExpiredPromoCode(time);
//        Then
        Mockito.verify(promoCodeRepository).findByExpiredDateLessThan(time);
        Mockito.verify(promoCodeRepository).deleteAll(getValidPromoCodes());


    }

    private List<PromoCode> buildListOfPromoCode() {
        List<PromoCode> promoCodes = getValidPromoCodes();
        PromoCode promoCode2 = getPromoCode();
        promoCode2.setValid(false);
        promoCodes.add(promoCode2);

        return promoCodes;
    }

    private List<PromoCode> getValidPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        PromoCode promoCode = getPromoCode();
        promoCode.setValid(true);
        promoCodes.add(promoCode);
        PromoCode promoCode1 = getPromoCode();
        promoCode1.setValid(true);
        promoCodes.add(promoCode1);
        return promoCodes;
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