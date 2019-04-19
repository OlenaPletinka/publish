package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import com.myFirstProject.myFirstProject.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArticleAndCategoryInfoScheduler {

    private Logger logger = LoggerFactory.getLogger(ArticleAndCategoryInfoScheduler.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // "${job.articleForDelete.initialDelay}"посилання на app.properties,щоб за необхідності інтервали змінювати одразу в настройках
    @Transactional(readOnly = true)
    @Scheduled(initialDelayString = "${job.articleAndCategory.initialDelay}", fixedRateString = "${job.articleAndCategory.fixedRate}")
    public void writeInfoAboutArticles(){
        Iterable<Article> articles = articleRepository.findAll();
        List<Article> allArticles = new ArrayList<>();
        articles.forEach(allArticles::add);
        int size = allArticles.size();
        logger.info(String.format("Total amount of articles is %d", size));
    }

    // "${job.articleForDelete.initialDelay}"посилання на app.properties,щоб за необхідності інтервали змінювати одразу в настройках
    @Transactional(readOnly = true)
    @Scheduled(initialDelayString = "${job.articleAndCategory.initialDelay}", fixedRateString = "${job.articleAndCategory.fixedRate}")
    public void writeInfoAboutCategories(){
        Iterable<Category> categories = categoryRepository.findAll();
        List<Category> allCategories = new ArrayList<>();
        categories.forEach(allCategories::add);
        int size = allCategories.size();
        logger.info(String.format("Total amount of category is %d", size));

    }
}
