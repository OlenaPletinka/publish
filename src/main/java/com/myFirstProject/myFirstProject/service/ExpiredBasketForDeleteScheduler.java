package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.Basket;
import com.myFirstProject.myFirstProject.model.DeletedBasket;
import com.myFirstProject.myFirstProject.repository.BasketRepository;
import com.myFirstProject.myFirstProject.repository.DeletedBasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpiredBasketForDeleteScheduler {

    private Logger logger = LoggerFactory.getLogger(ExpiredBasketForDeleteScheduler.class);

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private DeletedBasketRepository deletedBasketRepository;

    @Transactional
    @Scheduled(fixedRateString = "${job.expiredBasketForDelete.fixedRate}", initialDelayString = "${job.expiredBasketForDelete.initialDelay}")
    public void deleteBasketWithExpiredTime (){
        List<Basket> basketList = basketRepository.findByExpiredTimeLessThan(LocalDateTime.now());
        int size = basketList.size();
        logger.info(String.format("Find %d baskets with expired time", size));
        basketList.stream().forEach(basket -> {
            DeletedBasket deletedBasket = new DeletedBasket();
            Long id = basket.getId();
            deletedBasket.setIdDeletedBasket(id);
            deletedBasket.setExpiredTime(basket.getExpiredTime());
            deletedBasket.setUser(basket.getUser());
            deletedBasket.setSum(basket.getSum());
            basketRepository.delete(basket);
            deletedBasketRepository.save(deletedBasket);
            logger.info(String.format("Basket with id - %d was deleted", id));
            logger.info("{} was created", deletedBasket);
        });
        logger.info(String.format("%d baskets was deleted", size));
    }
}
