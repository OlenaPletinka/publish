package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateService {
    List<CurrencyRate> getRate();
    List<CurrencyRate> getRateFromRepository();
}
