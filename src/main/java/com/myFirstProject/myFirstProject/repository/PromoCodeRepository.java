package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.PromoCode;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromoCodeRepository extends CrudRepository<PromoCode, String> {
    List<PromoCode> findByExpiredLessThan(LocalDateTime dateTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PromoCode> findById(String id);

    List<PromoCode> findByTimeThenWasUsedBetween(LocalDateTime start, LocalDateTime end);
}
