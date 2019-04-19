package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.ArticleReq;
import com.myFirstProject.myFirstProject.dto.ArticleResp;

import java.awt.print.Pageable;
import java.util.List;

public interface ArticleService {
    Long saveArticle(ArticleReq articleReq);
    ArticleResp getById(Long id);
    void delete(Long id);
    ArticleResp update(ArticleReq articleReq);
    ArticleResp publish(Long id);
    List<ArticleResp> findByTitleSourceName (String title, String source, String name);
    List<ArticleResp> findByParam(String title, String source, String name);
    List<ArticleResp> findByTitle (String title);
    List<ArticleResp> findAllArticlesWithSameCategoryName(String name);
    List<ArticleResp> findByTitleAndSource(String title, String source);
    List<Long> findAllArticlesIdWithSameCategoryName(String name);
    List<ArticleResp> search(ArticleReq articleReq);
    List<ArticleResp> getArticlesFromBasketByUserId (Long id);
}
