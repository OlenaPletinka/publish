package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.PromoCode;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

public interface PromoCodeRepository extends CrudRepository<PromoCode, String> {
    List<PromoCode> findByExpiredLessThan(LocalDateTime dateTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    PromoCode findById (Long id);
}
