package com.myFirstProject.myFirstProject.controller.rest;

import com.myFirstProject.myFirstProject.dto.PromoCodeReq;
import com.myFirstProject.myFirstProject.exception.PromoCodeNotValidForSaveException;
import com.myFirstProject.myFirstProject.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/promocode/")
public class PromoCodeController {
    @Autowired
    private PromoCodeService promoCodeService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity savePromoCode(@RequestBody PromoCodeReq promoCodeReq) {
        validateCreate(promoCodeReq);
        String id = promoCodeService.save(promoCodeReq);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    private void validateCreate(PromoCodeReq promoCodeReq) {
        if (promoCodeReq == null|| promoCodeReq.getId() == null || promoCodeReq.getExpired() == null ||
                promoCodeReq.getPromoType() == null || promoCodeReq.getValue() == null ||
                    promoCodeReq.getExpired().compareTo(LocalDateTime.now())<1){
            throw new PromoCodeNotValidForSaveException("Promo code is not valid for save.");
        }
    }

}
