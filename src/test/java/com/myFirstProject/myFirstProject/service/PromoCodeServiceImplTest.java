package com.myFirstProject.myFirstProject.service;

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
        Mockito.verify(promoCodeRepository).deleteAll(buildListOfPromoCode());


    }

    private List<PromoCode> buildListOfPromoCode() {
        List<PromoCode> promoCodes = new ArrayList<>();
        PromoCode promoCode = new PromoCode();
        promoCode.setValue(BigDecimal.TEN);
        promoCodes.add(promoCode);

        return promoCodes;
    }
}