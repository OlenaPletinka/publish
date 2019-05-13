package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.PromoCode;
import com.myFirstProject.myFirstProject.repository.PromoCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sun.tools.internal.xjc.reader.Ring.add;

@Component
public class PromoCodePerMonthScheduler {
    private Logger logger = LoggerFactory.getLogger(PromoCodePerMonthScheduler.class);
    private PromoCodeRepository promoCodeRepository;
//    використовуємо клок для того щоб протестувати локал дейт тайм
//    в аплікейшен конфіг створений бін для того щоб автовайрити
    private Clock clock;

    @Autowired
    public void setPromoCodeRepository(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Autowired
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional(readOnly = true)
    public void infoAboutUsedPromoCode() {
        LocalDateTime time = LocalDateTime.now(clock);
        LocalDateTime initial = time.minusMonths(1L);
        LocalDateTime start = initial.withDayOfMonth(1);
        LocalDateTime end = time.minusDays(1L);
        List<PromoCode> promoCodes = promoCodeRepository.findByTimeThenWasUsedBetween(start, end);
        if (promoCodes.isEmpty()) {
            logger.info(String.format("From %s to %s promo codes did not use."
                    , start.toString(), end.toString()));
        } else {
            BigDecimal discountPerMonth = promoCodes.stream()
                    .map(PromoCode::getTotalDiscount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            logger.info(String.format("From %s to %s total amount of discount is %s. Users used $d promo codes."
                    , start.toString(), end.toString(), discountPerMonth, promoCodes.size()));
        }
    }
}
