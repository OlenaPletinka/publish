package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.CurrencyRate;

import java.util.List;

public interface CurrencyConverter {
    List<CurrencyRate> convert (CurrencyRateResp currencyRateResp);
}
