package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.dto.BasketReq;
import com.myFirstProject.myFirstProject.exception.PromoCodeNotFoundException;
import com.myFirstProject.myFirstProject.exception.PromoCodeNotValidException;
import com.myFirstProject.myFirstProject.exception.SumFromBasketReqIsNotValidException;
import com.myFirstProject.myFirstProject.model.Basket;
import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.BasketRepository;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
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
import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {
    @Value("${cost.one.article}")
    private BigDecimal cost;

    private Logger logger = LoggerFactory.getLogger(BasketServiceImpl.class);
    private BasketRepository basketRepository;
    private AccountService accountService;
    private ReqConverterService reqConverterService;
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository, AccountService accountService, ReqConverterService reqConverterService, PromoCodeRepository promoCodeRepository) {
        this.basketRepository = basketRepository;
        this.accountService = accountService;
        this.reqConverterService = reqConverterService;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    @Transactional
    public Long saveBasket(BasketReq basketReq) {
        logger.info("Get order to update {}", basketReq);
        sumValidation(basketReq);
        PromoCode promoCode = getPromoCodeFromDB(basketReq);
        Basket order = reqConverterService.convert(basketReq);
        order.setPromoCode(promoCode);
        LocalDateTime time = LocalDateTime.now();
        order.setExpiredTime(time.plusDays(1L));
        Basket savedOrder = basketRepository.save(order);
        Long id = savedOrder.getId();
        logger.info("Basket saved with id {}", id);
        accountService.payForArticles(basketReq.getSum(), basketReq.getUser().getId(), promoCode);
        promoCode.setValid(false);
        logger.info(String.format("%s was payed for order", basketReq.getSum()));

        return id;
    }

    private PromoCode getPromoCodeFromDB(BasketReq basketReq) {
        PromoCode promoCode = getPromoCode(basketReq);
        if (promoCode == null) {
            throw new PromoCodeNotFoundException(String.format("Promo code with id - %s not found", basketReq.getPromoCodeId()));
        } else if (!promoCode.getValid()) {
            throw new PromoCodeNotValidException(String.format("PromoCode with id - %s have been used", basketReq.getId()));
        } else if (promoCode.getExpired().compareTo(LocalDateTime.now()) < 0) {
            throw new PromoCodeNotValidException(String.format("PromoCode with id - %s expired", basketReq.getId()));
        }
        return promoCode;
    }

    private PromoCode getPromoCode(BasketReq basketReq) {
        PromoCode promoCode = promoCodeRepository.findById(basketReq.getId());

        return promoCode;
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
            if (basket.getSum() != null) {
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
