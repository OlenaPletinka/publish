package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.model.CurrencyRate;
import com.myFirstProject.myFirstProject.repository.CurrencyRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CurrencyRateScheduler {
    private Logger logger = LoggerFactory.getLogger(CurrencyRateScheduler.class);
    private CurrencyRateRepository currencyRateRepository;
    private CurrencyRateService currencyRateService;

    @Autowired
    public void setCurrencyRateRepository(CurrencyRateRepository currencyRateRepository) {
        this.currencyRateRepository = currencyRateRepository;
    }

    @Autowired
    public void setCurrencyRateService(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @Transactional()
    @Scheduled(initialDelayString = "${job.currencyRateScheduler.initialDelay}", fixedRateString = "${job.currencyRateScheduler.fixedRate}")
    public void updateCurrencyRate() {
        List<CurrencyRate> currencyRates = currencyRateService.getRate();
        currencyRateRepository.saveAll(currencyRates);
        logger.info("Update currency rates in DB");
    }

}
