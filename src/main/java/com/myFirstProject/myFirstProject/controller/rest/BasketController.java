package com.myFirstProject.myFirstProject.controller.rest;

import com.myFirstProject.myFirstProject.dto.BasketReq;
import com.myFirstProject.myFirstProject.exception.OrderNotValidForSave;
import com.myFirstProject.myFirstProject.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/basket/")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveOrder (@RequestBody BasketReq basketReq){
        orderValidForSave(basketReq);
        basketService.saveBasket(basketReq);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void orderValidForSave(BasketReq basketReq) {
        if (basketReq.getId()!=null|| basketReq.getUser()==null|| basketReq.getArticleList().isEmpty()|| basketReq.getArticleList()==null||basketReq.getSum()==null){
            throw new OrderNotValidForSave("Basket is not valid for save");
        }
    }
}
