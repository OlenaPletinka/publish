package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.Payment;
import com.myFirstProject.myFirstProject.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TaxServiceScheduler {
    private Logger logger = LoggerFactory.getLogger(TaxServiceScheduler.class);
    private PaymentRepository paymentRepository;
    private AccountService accountService;

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 1 * *")
    //cron використовується в скедулері для вказання часових рамок - періодичності виконання дії не залежно від вмикання
    // додаткуб - містить 6-7 символів (сек хв год день міс рік)
    //initialDelay fixedRate на відміну від них
    public void payTax (){
        LocalDate initial = LocalDate.now().minusMonths(1L);
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        List<Payment> paymentPerMonth = paymentRepository.findByTimeOfPaymentBetween(start, end);
        BigDecimal taxPerMonth = accountService.calculateTaxPerMonth(paymentPerMonth);
        logger.info(String.format("Company must pay %s as tax", taxPerMonth));
    }

}
