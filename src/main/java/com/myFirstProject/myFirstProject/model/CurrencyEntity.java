package com.myFirstProject.myFirstProject.model;

import com.myFirstProject.myFirstProject.enums.Currency;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data
public class CurrencyEntity {
    @Column
    @Id
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
