package com.myFirstProject.myFirstProject.model;

import com.myFirstProject.myFirstProject.enums.Currency;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data
public class Basket {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private BigDecimal sum;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    LocalDateTime expiredTime;

    @ManyToMany
    @JoinTable(name = "ordered_articles", joinColumns = {@JoinColumn(name = "basket_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "article_id", referencedColumnName = "id")})
    private List<Article> articleList = new ArrayList<>();

    @OneToOne
    @JoinColumn
    private PromoCode promoCode;

    @Override
    public String toString() {
        return "Basket{" +
                "id=" + id +
                '}';
    }
}
