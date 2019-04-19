package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.RespConverterService;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.dto.PaymentResp;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.Account;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.Payment;
import com.myFirstProject.myFirstProject.repository.AccountRepository;
import com.myFirstProject.myFirstProject.repository.PaymentRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RespConverterService respConverterService;

    @Override
    @Transactional
    public List<PaymentResp> getAllPaymentByUserId(Pageable pageable, Long userId) {
        validation(userId);
        Account accountByUserId = accountRepository.findAccountByUserId(userId);
        Page<Payment> allPaymentByAccount = paymentRepository.findAllByAccount(accountByUserId, pageable);
        return allPaymentByAccount.stream().map(payment -> respConverterService.convertPaymentToResp(payment)).collect(Collectors.toList());

    }

    @Override
    public List<PaymentResp> findAllBySum(BigDecimal sum, Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAllBySum(sum, pageable);
        List<PaymentResp> paymentResps = payments.stream().map(payment -> respConverterService.convertPaymentToResp(payment)).collect(Collectors.toList());
        return paymentResps;
    }

    private void validation(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new UserNotFoundException(String.format("User with id - %d not find", userId));
        }
    }
}
