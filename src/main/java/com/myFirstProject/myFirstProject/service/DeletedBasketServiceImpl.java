package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.DeletedBasket;
import com.myFirstProject.myFirstProject.repository.DeletedBasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeletedBasketServiceImpl implements DeletedBasketService {
    private Logger logger = LoggerFactory.getLogger(DeletedBasketServiceImpl.class);

    @Autowired
    private DeletedBasketRepository deletedBasketRepository;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAmountOfSpentMoneyFromDeletedBaskets(Long userId) {
        BigDecimal amountOfSpentMoneyFromDeletedBaskets = BigDecimal.ZERO;
        List<DeletedBasket> deletedBasketsByUserId = deletedBasketRepository.findDeletedBasketsByUserId(userId);
        for (DeletedBasket deletedBasket : deletedBasketsByUserId) {
            amountOfSpentMoneyFromDeletedBaskets = amountOfSpentMoneyFromDeletedBaskets.add(deletedBasket.getSum());
        }
        logger.info(String.format("User with id - %d spent - %s in deletedBasket", userId, amountOfSpentMoneyFromDeletedBaskets));

        return amountOfSpentMoneyFromDeletedBaskets;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revenueFromDeletedBaskets() {
        BigDecimal revenueFromDeletedBaskets = BigDecimal.ZERO;
        Iterable<DeletedBasket> deletedBaskets = deletedBasketRepository.findAll();
        List<DeletedBasket>deletedBasketList = new ArrayList<>();
        deletedBaskets.forEach(deletedBasketList::add);
        for (DeletedBasket deletedBasket : deletedBasketList) {
            if (deletedBasket.getSum()!=null) {
                revenueFromDeletedBaskets = revenueFromDeletedBaskets.add(deletedBasket.getSum());
            }
        }
        logger.info(String.format("Company earn - %s from deletedBaskets", revenueFromDeletedBaskets));
        return revenueFromDeletedBaskets;
    }


}
