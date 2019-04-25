package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.RespConverterService;
import com.myFirstProject.myFirstProject.converter.RespConverterServiceImpl;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.exception.ArticleNotFoundException;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Basket;
import com.myFirstProject.myFirstProject.model.User;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private RespConverterService respConverterService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void getByIdSuccessStory() {
//        GIVEN
        Article article = buildArticle();
        Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
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
    public void getByIdWithException() {
//        GIVEN
        Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleServiceImpl articleService = new ArticleServiceImpl();
        articleService.setArticleRepository(articleRepository);
//        WHEN
        ArticleResp actualResult = articleService.getById(1L);
    }

    @Test
    public void getArticlesFromBasketByUserIdBestCase() {
//        GIVEN
        User user = getUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ArticleServiceImpl articleService = new ArticleServiceImpl();
        articleService.setUserRepository(userRepository);
        articleService.setRespConverterService(respConverterService);
        ArticleResp articleResp = new ArticleResp(2L);

        Mockito.when(respConverterService.convertToResp(
                user.getOrders().get(0).getArticleList().get(0)
        )).thenReturn(articleResp);

//        WHEN
        List<ArticleResp> actualResult = articleService.getArticlesFromBasketByUserId(1L);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(articleResp, actualResult.get(0));
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        List<Basket> orders = new ArrayList<>();
        Basket basket = new Basket();
        basket.setId(11L);
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setId(10L);
        articles.add(article);
        basket.setArticleList(articles);
        orders.add(basket);
        user.setOrders(orders);
        return user;
    }

    @Test(expected = UserNotFoundException.class)
    public void getArticlesFromBasketByUserIdWithException() {
//        GIVEN
        User user = getUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ArticleServiceImpl articleService = new ArticleServiceImpl();
        articleService.setUserRepository(userRepository);
        articleService.setRespConverterService(respConverterService);

//        WHEN
        List<ArticleResp> actualResult = articleService.getArticlesFromBasketByUserId(1L);
    }
}
//        GIVEN
//        WHEN
//        THEN