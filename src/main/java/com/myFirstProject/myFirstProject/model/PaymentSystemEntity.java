package com.myFirstProject.myFirstProject.model;

import com.myFirstProject.myFirstProject.enums.PaymentSystem;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@Data
public class PaymentSystemEntity {
    @Column
    @Id
    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;
}
