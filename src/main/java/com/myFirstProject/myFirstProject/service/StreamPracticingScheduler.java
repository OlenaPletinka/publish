package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Source;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StreamPracticingScheduler {
    private Logger logger = LoggerFactory.getLogger(StreamPracticingScheduler.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 3600000, initialDelay = 100)
    public void practicing() {
        Iterable<Article> all = articleRepository.findAll();
        List<Article> articles = new ArrayList<>();
        all.forEach(articles::add);

        //згрупувати по сорсу
        Map<Source, List<Article>> collect = articles.stream().collect(Collectors.groupingBy(Article::getSource));
        logger.info(String.format("Articles group in map by source, size - %d", collect.size()));

        //фідфільтрувати опубліковані
        List<Article> list = articles.stream().filter(Article::isPublish).collect(Collectors.toList());
        logger.info(String.format("Find %d articles which was published", list.size()));

        //фідфільтрувати не опубліковані
        List<Article> collect1 = articles.stream().filter(article -> !article.isPublish()).collect(Collectors.toList());
        logger.info(String.format("Find %d articles which was not published", collect1.size()));

        //показати всі унікальні соурси
        List<Source> collect2 = articles.stream().map(article -> article.getSource()).distinct().collect(Collectors.toList());
        logger.info(String.format("Find %d unique sources", collect2.size()));

        if (articles.stream().anyMatch(article -> article.isPublish())) {
            logger.info("One or more articles from the list are published");
        } else {
            logger.info("No one articles from the list are published");
        }

        if (articles.stream().allMatch(article -> article.isPublish())) {
            logger.info("All articles from the list are published");
        } else {
            logger.info("Not avery articles from the list are published");
        }


    }
}