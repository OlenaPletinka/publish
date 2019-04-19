package com.myFirstProject.myFirstProject.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table
@Data
public class CurrencyRate {
//композитивний ключ
    @EmbeddedId
    private CompositeId compositeId = new CompositeId();

    @Column
    private BigDecimal rate;


    //Oбщвязково :
    //Класс должен быть public
//У класса должен быть публичный конструктор по умолчанию.
//Класс должен (корректно) реализовывать собственные equals()  и hashCode()
//Класс должен реализовывать Serializable
    // @Embeddable вказує на комбінований праймері кей
    @Embeddable
    @Data
    public static class CompositeId implements Serializable {
        @ManyToOne
        @JoinColumn
        private CurrencyEntity original;

        @ManyToOne
        @JoinColumn
        private CurrencyEntity destination;
    }
}
