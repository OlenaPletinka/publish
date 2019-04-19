package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.PaymentResp;
import com.myFirstProject.myFirstProject.exception.NotValidUserIdException;
import com.myFirstProject.myFirstProject.model.CurrencyEntity;
import com.myFirstProject.myFirstProject.model.Payment;
import com.myFirstProject.myFirstProject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/payment/")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    //Pageable використовується для пагінації отриманої відповіді
    // в репозиторії - наслідуємся від PagingAndSortingRepository<Payment, Long>
    @RequestMapping(value = "{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity  getAllPaymentByUserId (Pageable pageable, @PathVariable("userId") Long userId) throws JsonProcessingException {
        validationOfUserId(userId);
        List<PaymentResp> payments = paymentService.getAllPaymentByUserId(pageable, userId);

        return ResponseEntity.ok(objectMapper.writeValueAsString(payments));
    }

    private void validationOfUserId(Long userId) {
        if (userId==null){
            throw new NotValidUserIdException("User id is not correct.");
        }
    }

    //required -  Чи потрібний параметр.
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAllByCurrencyEntity(Pageable pageable, @RequestParam(name = "sum", required = false)BigDecimal sum) throws JsonProcessingException {
        List<PaymentResp> articleResps = paymentService.findAllBySum(sum, pageable);
        return ResponseEntity.ok(objectMapper.writeValueAsString(articleResps));
    }
}
