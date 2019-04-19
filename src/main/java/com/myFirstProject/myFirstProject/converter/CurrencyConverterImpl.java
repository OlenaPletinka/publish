package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.CurrencyRateResp;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.CurrencyRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConverterImpl implements CurrencyConverter {
    @Override
    public List<CurrencyRate> convert(CurrencyRateResp currencyRateResp) {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        String base = currencyRateResp.getBase();
        Map<String, BigDecimal> rates = currencyRateResp.getRates();
        rates.entrySet().stream().forEach(entry -> {
            CurrencyRate currencyRate = new CurrencyRate();
            CurrencyRate.CompositeId compositeId = new CurrencyRate.CompositeId();
            CurrencyEntity original = new CurrencyEntity();
            original.setCurrency(Currency.valueOf(base));
            compositeId.setOriginal(original);
            CurrencyEntity destination = new CurrencyEntity();
            destination.setCurrency(Currency.valueOf(entry.getKey()));
            compositeId.setDestination(destination);
            currencyRate.setCompositeId(compositeId);
            currencyRate.setRate(entry.getValue());
            currencyRates.add(currencyRate);
        });
        return currencyRates;
    }
}
