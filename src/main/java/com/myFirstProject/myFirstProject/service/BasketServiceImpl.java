package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.dto.BasketReq;
import com.myFirstProject.myFirstProject.exception.PromoCodeNotValidException;
import com.myFirstProject.myFirstProject.exception.SumFromBasketReqIsNotValidException;
import com.myFirstProject.myFirstProject.model.Basket;
import com.myFirstProject.myFirstProject.repository.BasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {
    private Logger logger = LoggerFactory.getLogger(BasketServiceImpl.class);

    @Value("${cost.one.article}")
    private BigDecimal cost;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReqConverterService reqConverterService;

    @Override
    @Transactional
    public Long saveBasket(BasketReq basketReq) {
        logger.info("Get order to update {}", basketReq);
        sumValidation(basketReq);
        promoCodeValidation(basketReq);
        Basket order = reqConverterService.convert(basketReq);
        LocalDateTime time = LocalDateTime.now();
        order.setExpiredTime(time.plusDays(1L));
        Basket order1 = basketRepository.save(order);
        Long id = order1.getId();
        logger.info("Basket saved with id {}", id);
        accountService.payForArticles(basketReq.getSum(), basketReq.getUser().getId(), basketReq.getPromoCode());
        basketReq.getPromoCode().setValid(false);
        logger.info(String.format("%s was payed for order", basketReq.getSum()));

        return id;
    }

    private void promoCodeValidation(BasketReq basketReq) {
        if (!basketReq.getPromoCode().getValid()){
            throw new PromoCodeNotValidException(String.format("PromoCode with id - %s have been used", basketReq.getId()));
        }else if (basketReq.getPromoCode().getExpiredDate().compareTo(LocalDateTime.now())<0){
            throw new PromoCodeNotValidException(String.format("PromoCode with id - %s expired", basketReq.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAmountOfSpentMoneyFromBaskets(Long userId) {
        BigDecimal amountOfSpentMoneyFromBaskets = BigDecimal.ZERO;
        List<Basket> basketsByUserId = basketRepository.findBasketsByUserId(userId);
        for (Basket basket : basketsByUserId) {
            amountOfSpentMoneyFromBaskets = amountOfSpentMoneyFromBaskets.add(basket.getSum());
        }
        logger.info(String.format("User with id - %d spent - %s in baskets", userId, amountOfSpentMoneyFromBaskets));

        return amountOfSpentMoneyFromBaskets;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revenueFromBaskets() {
        BigDecimal revenueFromBaskets = BigDecimal.ZERO;
        Iterable<Basket> baskets = basketRepository.findAll();
        List<Basket> basketList = new ArrayList<>();
        baskets.forEach(basketList::add);
        for (Basket basket : basketList) {
            if (basket.getSum()!=null) {
                revenueFromBaskets = revenueFromBaskets.add(basket.getSum());
            }
        }
        logger.info(String.format("Company earn - %s from baskets", revenueFromBaskets));

        return revenueFromBaskets;
    }

    private void sumValidation(BasketReq basketReq) {
        BigDecimal sum = basketReq.getSum();
        logger.info(String.format("Sum from basketReq - %s", sum));
        int size = basketReq.getArticleList().size();
        BigDecimal validSum = cost.multiply(new BigDecimal(size));
        logger.info(String.format("Valid sum - %s", validSum));
        if (!sum.equals(validSum)) {
            throw new SumFromBasketReqIsNotValidException("Sum from basketReq is not valid for payment");
        }

    }

}
