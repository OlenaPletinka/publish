package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.DeletedBasket;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeletedBasketRepository extends CrudRepository<DeletedBasket, Long> {
    List<DeletedBasket> findDeletedBasketsByUserId(Long userId);
}
