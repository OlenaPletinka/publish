package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.RespConverterService;
import com.myFirstProject.myFirstProject.converter.RespConverterServiceImpl;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.exception.ArticleNotFoundException;
import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private RespConverterServiceImpl respConverterService;

    @Test
    public void getByIdSuccessStory() {
//        GIVEN
        Article article = buildArticle();
        Mockito.when(articleRepository.findById(1L)).thenReturn( Optional.of(article));
        Mockito.when(respConverterService.convertToResp(article)).thenReturn(new ArticleResp(1L));

        ArticleServiceImpl articleService = new ArticleServiceImpl();
        articleService.setRespConverterService(respConverterService);
        articleService.setArticleRepository(articleRepository);
//        WHEN
        ArticleResp actualResult = articleService.getById(1L);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(actualResult.getId().longValue(), 1L);
    }

    public Article buildArticle() {
        Article article = new Article();
        article.setId(1L);
        return article;
    }

    @Test(expected = ArticleNotFoundException.class)
    public void getByIdWithException(){
//        GIVEN
        Mockito.when(articleRepository.findById(1L)).thenReturn( Optional.empty());

        ArticleServiceImpl articleService = new ArticleServiceImpl();
        articleService.setArticleRepository(articleRepository);
//        WHEN
        ArticleResp actualResult = articleService.getById(1L);
    }
}
//        GIVEN
//        WHEN
//        THEN