package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.CurrencyRate;
import com.myFirstProject.myFirstProject.repository.CurrencyRateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRateSchedulerTest {
    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private CurrencyRateService currencyRateService;

    @Test
    public void updateCurrencyRate() {
//        GIVEN
        CurrencyRateScheduler currencyRateScheduler = new CurrencyRateScheduler();
        currencyRateScheduler.setCurrencyRateRepository(currencyRateRepository);
        currencyRateScheduler.setCurrencyRateService(currencyRateService);

        Mockito.when(currencyRateService.getRate()).thenReturn(getListCurrencyRate());
//        WHEN
        currencyRateScheduler.updateCurrencyRate();
//        THEN
        Mockito.verify(currencyRateRepository).saveAll(eq(getListCurrencyRate()));
    }

    private List<CurrencyRate> getListCurrencyRate() {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRates.add(currencyRate);

        return currencyRates;
    }
}