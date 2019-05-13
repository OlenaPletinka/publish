package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class BasketReq {
    private Long id;
    private User user;
    private BigDecimal sum;
    private Currency currency;
    private List<Article> articleList = new ArrayList<>();
    private String promoCodeId;

    @Override
    public String toString() {
        return "BasketReq{" +
                "id=" + id +
                ", user=" + user +
                ", sum=" + sum +
                ", currency=" + currency +
                ", articleList=" + articleList +
                ", promoCodeId='" + promoCodeId + '\'' +
                '}';
    }
}
