package com.myFirstProject.myFirstProject.model;

import com.myFirstProject.myFirstProject.enums.PromoType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class PromoCode {
    @Column
    @Id
    private String id;

    @Column
    @Enumerated(EnumType.STRING)
    private PromoType promoType;

    @Column
    private BigDecimal value;

    @Column
    private Boolean valid = true;

    @Column
    private LocalDateTime expiredDate;
}
