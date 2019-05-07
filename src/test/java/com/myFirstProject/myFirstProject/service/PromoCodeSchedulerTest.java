package com.myFirstProject.myFirstProject.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PromoCodeSchedulerTest {
    @Mock
    private PromoCodeService promoCodeService;

    @Test
    public void deleteExpiredPromoCode() {
//        Given
        PromoCodeScheduler promoCodeScheduler = new PromoCodeScheduler();
        promoCodeScheduler.setPromoCodeService(promoCodeService);
//        When
        promoCodeScheduler.deleteExpiredPromoCode();
//        Then
        Mockito.verify(promoCodeService).deleteExpiredPromoCode(any());
    }
}