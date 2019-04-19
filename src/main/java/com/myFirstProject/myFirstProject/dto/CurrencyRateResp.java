package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Data
public class CurrencyRateResp {
    private String base;
    private Map<String, BigDecimal>rates = new HashMap<>();
}
