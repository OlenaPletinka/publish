package com.myFirstProject.myFirstProject.model;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Data
public class Payment {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paymentSystem", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private PaymentSystemEntity paymentSystemEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name ="account_id", nullable = false)
    private Account account;

    @Column
    private LocalDateTime timeOfPayment;

    @Column
    private BigDecimal sum;

    @OneToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private CurrencyEntity currencyEntity;

    @Column
    private BigDecimal rate;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentSystemEntity=" + paymentSystemEntity +
                ", account=" + account +
                ", timeOfPayment=" + timeOfPayment +
                ", sum=" + sum +
                ", currencyEntity=" + currencyEntity +
                ", rate=" + rate +
                '}';
    }
}
