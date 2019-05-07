package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.PromoCode;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PromoCodeRepository extends CrudRepository<PromoCode, String> {
    List<PromoCode> findByExpiredDateLessThan (LocalDateTime dateTime);
}
