package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.CurrencyConverter;
import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.exception.CurrencyRateRespNotGetException;
import com.myFirstProject.myFirstProject.model.CurrencyRate;
import com.myFirstProject.myFirstProject.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private Logger logger = LoggerFactory.getLogger(CurrencyRateServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyConverter currencyConverter;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Override
    public List<CurrencyRate> getRate() {
//        try {
            CurrencyRateResp currencyRateResp = restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base=USD", CurrencyRateResp.class);
            if (currencyRateResp != null) {
                logger.info("Get currency rates from - api.exchangeratesapi.io");

                return  currencyConverter.convert(currencyRateResp);
            } else {
                throw new CurrencyRateRespNotGetException("Do not get currencyRate response from - api.exchangeratesapi.io");
            }
//        }catch (RestClientException Ex){
//            logger.info("Error while extracting currencyRateResp.");
//        }
    }

    @Override
    public List<CurrencyRate> getRateFromRepository() {
        Iterable<CurrencyRate> currencyRates = currencyRateRepository.findAll();
        List<CurrencyRate> result = new ArrayList<>();
        currencyRates.forEach(result::add);

        return result;
    }
}
