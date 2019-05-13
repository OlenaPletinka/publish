package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.Account;
import com.myFirstProject.myFirstProject.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
    List<Payment> findByTimeOfPaymentBetween (LocalDateTime start, LocalDateTime end);
    Page<Payment> findAllByAccount(Account account, Pageable pageable);

    Page<Payment> findAllBySum(BigDecimal sum, Pageable pageable);
}
