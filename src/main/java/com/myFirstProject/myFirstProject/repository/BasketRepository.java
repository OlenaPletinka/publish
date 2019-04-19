package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.Basket;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BasketRepository extends CrudRepository<Basket, Long> {
    List<Basket> findByExpiredTimeLessThan(LocalDateTime dateTime);
    List<Basket> findBasketsByUserId(Long userId);
}
