package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.enums.PromoType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromoCodeReq {
    private String id;
    private PromoType promoType;
    private BigDecimal value;
    private LocalDateTime expired;
}
