package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.CurrencyConverter;
import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.exception.CurrencyRateRespNotGetException;
import com.myFirstProject.myFirstProject.model.CurrencyRate;
import com.myFirstProject.myFirstProject.repository.CurrencyRateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRateServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CurrencyConverter currencyConverter;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Test
    public void getRateBestCase() {
//        GIVEN
        CurrencyRateServiceImpl currencyRateService = new CurrencyRateServiceImpl();
        currencyRateService.setCurrencyConverter(currencyConverter);
        currencyRateService.setRestTemplate(restTemplate);

        CurrencyRateResp currencyRateResp = new CurrencyRateResp();

        Mockito.when(restTemplate.getForObject(
                "https://api.exchangeratesapi.io/latest?base=USD", CurrencyRateResp.class))
                .thenReturn(currencyRateResp);
        Mockito.when(currencyConverter.convert(currencyRateResp)).thenReturn(new ArrayList<CurrencyRate>());
//        WHEN
        List<CurrencyRate> actualResult = currencyRateService.getRate();
//        THEN
        Assert.assertNotNull(actualResult);
    }

    @Test(expected = CurrencyRateRespNotGetException.class)
    public void getRateWithException() {
//        GIVEN
        CurrencyRateServiceImpl currencyRateService = new CurrencyRateServiceImpl();
        currencyRateService.setRestTemplate(restTemplate);

        CurrencyRateResp currencyRateResp = new CurrencyRateResp();

        Mockito.when(restTemplate.getForObject(
                "https://api.exchangeratesapi.io/latest?base=USD", CurrencyRateResp.class))
                .thenReturn(null);
//        WHEN
        List<CurrencyRate> actualResult = currencyRateService.getRate();
    }

    @Test
    public void getRateFromRepository() {
//        GIVEN
        CurrencyRateServiceImpl currencyRateService = new CurrencyRateServiceImpl();
        currencyRateService.setCurrencyRateRepository(currencyRateRepository);
        Iterable<CurrencyRate> currencyRates = new ArrayList<>();
        Mockito.when(currencyRateRepository.findAll()).thenReturn(currencyRates);
//        WHEN
        List<CurrencyRate> actualResult = currencyRateService.getRateFromRepository();
//        THEN
        Assert.assertNotNull(actualResult);
    }


}