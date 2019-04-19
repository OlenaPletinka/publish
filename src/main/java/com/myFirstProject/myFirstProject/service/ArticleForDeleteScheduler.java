package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleForDeleteScheduler {

    private Logger logger = LoggerFactory.getLogger(ArticleForDeleteScheduler.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BasketService basketService;

    // "${job.articleForDelete.initialDelay}"посилання на app.properties,щоб за необхідності інтервали змінювати одразу в настройках
    @Transactional
    @Scheduled(initialDelayString = "${job.articleForDelete.initialDelay}", fixedRateString = "${job.articleForDelete.fixedRate}")
    public void deleteByTimestampLessThanAndIsPublishIs (){
        List<Article> articles = articleRepository.findByTimestampLessThanAndIsPublishIs(LocalDateTime.now().minus(Duration.ofDays(1L)), false);
        int size = articles.size();
        int count = 0;
        logger.info(String.format("%d articles was find that was created more when day ago and was not publish", size));
        for (Article article : articles) {
            if(article.getBaskets().isEmpty()){
                Long id = article.getId();
                logger.info(String.format("Article with id - %d was not found at any baskets.", id));
                articleRepository.delete(article);
                logger.info(String.format("Article with id - %d was deleted", id));
                count ++;
            }
        }
        logger.info(String.format("%d articles was deleted", count));
    }

}
