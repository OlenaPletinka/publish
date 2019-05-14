package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.enums.PromoType;
import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class PromoCodePerMonthSchedulerTest {
    private final static LocalDate LOCAL_DATE = LocalDate.of(2016, 10, 12);
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private Clock clock;
    private Clock fixedClock;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        //tell your tests to return the specified LOCAL_DATE when calling LocalDate.now(clock)
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @Test
    public void infoAboutUsedPromoCode() {
//        Given
        PromoCodePerMonthScheduler promoCodePerMonthScheduler = new PromoCodePerMonthScheduler();
        promoCodePerMonthScheduler.setPromoCodeRepository(promoCodeRepository);
        promoCodePerMonthScheduler.setClock(clock);
//        задаємоформат часу
//        спочатку подебажила тест потім отримані значення перенесла сюди як енд і старт
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2016-09-01 00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2016-10-11 00:00", formatter);
        Mockito.when(promoCodeRepository.findByTimeThenWasUsedBetween(start, end)).thenReturn(buildPromoCodes());
//        When
        promoCodePerMonthScheduler.infoAboutUsedPromoCode();
//        Then
        Mockito.verify(promoCodeRepository).findByTimeThenWasUsedBetween(start, end);
    }

    private List<PromoCode> buildPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodes.add(buildPromoCode("PROMO_10", BigDecimal.TEN, PromoType.PERCENT, LocalDateTime.now().plusMonths(10), BigDecimal.ONE));
        promoCodes.add(buildPromoCode("PROMO_1", BigDecimal.ONE, PromoType.MONEY, LocalDateTime.now().plusMonths(10), BigDecimal.ONE));
        return promoCodes;
    }

    private PromoCode buildPromoCode(String id, BigDecimal value, PromoType promoType, LocalDateTime expired, BigDecimal discount) {
        PromoCode promoCode = new PromoCode();
        promoCode.setId(id);
        promoCode.setValue(value);
        promoCode.setPromoType(promoType);
        promoCode.setExpired(expired);
        promoCode.setTotalDiscount(discount);
        return promoCode;
    }

    @Test
    public void infoAboutUsedPromoCodeIsEmpty() {
        //        Given
        PromoCodePerMonthScheduler promoCodePerMonthScheduler = new PromoCodePerMonthScheduler();
        promoCodePerMonthScheduler.setPromoCodeRepository(promoCodeRepository);
        promoCodePerMonthScheduler.setClock(clock);
//        задаємоформат часу
//        спочатку подебажила тест потім отримані значення перенесла сюди як енд і старт
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse("2016-09-01 00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2016-10-11 00:00", formatter);
        Mockito.when(promoCodeRepository.findByTimeThenWasUsedBetween(start, end)).thenReturn(new ArrayList<PromoCode>());
//        When
        promoCodePerMonthScheduler.infoAboutUsedPromoCode();
//        Then
        Mockito.verify(promoCodeRepository).findByTimeThenWasUsedBetween(start, end);
    }
}