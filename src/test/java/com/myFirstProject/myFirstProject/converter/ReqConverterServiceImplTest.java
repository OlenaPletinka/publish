package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.*;
import com.myFirstProject.myFirstProject.model.*;
import com.myFirstProject.myFirstProject.service.PasswordMD5Service;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.myFirstProject.myFirstProject.enums.Currency.USD;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ReqConverterServiceImplTest {
    @Mock
    private PasswordMD5Service passwordMD5Service;

    @Test
    public void convert() {
//        GIVEN
        ArticleReq articleReq = buildArticleReq();
        ReqConverterServiceImpl reqConverterService = new ReqConverterServiceImpl();
//        WHEN
        Article actualResult = reqConverterService.convert(articleReq);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(1L, actualResult.getId().longValue());
        Assert.assertEquals("One", actualResult.getTitle());
        Assert.assertEquals("Two", actualResult.getBody());
        Assert.assertTrue(actualResult.isPublish());
        Assert.assertNotNull(actualResult.getCategory());
        Assert.assertNotNull(actualResult.getSource());
        Assert.assertEquals(1L, actualResult.getSource().getId().longValue());
        Assert.assertEquals("Two", actualResult.getSource().getName());
        Assert.assertEquals(1L, actualResult.getCategory().getId().longValue());
        Assert.assertEquals("One", actualResult.getCategory().getName());
    }

    private ArticleReq buildArticleReq() {
        ArticleReq articleReq = new ArticleReq();
        articleReq.setId(1L);
        articleReq.setTitle("One");
        articleReq.setBody("Two");
        articleReq.setPublish(true);
        articleReq.setCategory(buildCategoryReq());
        articleReq.setSource(buildSourceReq());
        return articleReq;
    }

    @Test
    public void convert1() {
//        GIVEN
        CategoryReq categoryReq = buildCategoryReq();
        ReqConverterServiceImpl reqConverterServiceImpl = new ReqConverterServiceImpl();
//        WHEN
        Category actualResult = reqConverterServiceImpl.convert(categoryReq);
//        THEN

        Assert.assertNotNull(actualResult);
        Assert.assertEquals(1L, actualResult.getId().longValue());
        Assert.assertEquals("One", actualResult.getName());

    }

    public CategoryReq buildCategoryReq() {
        CategoryReq categoryReq = new CategoryReq();
        categoryReq.setId(1L);
        categoryReq.setName("One");
        return categoryReq;
    }

    @Test
    public void convert2() {
        //        GIVEN
        SourceReq sourceReq = buildSourceReq();
        ReqConverterServiceImpl reqConverterService = new ReqConverterServiceImpl();
//        WHEN
        Source actualResult = reqConverterService.convert(sourceReq);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(1L, actualResult.getId().longValue());
        Assert.assertEquals("Two", actualResult.getName());
    }

    private SourceReq buildSourceReq() {
        SourceReq sourceReq = new SourceReq();
        sourceReq.setId(1L);
        sourceReq.setName("Two");
        return sourceReq;
    }

    @Test
    public void convert3() {
//        GIVEN
        ReqConverterServiceImpl reqConverterService = new ReqConverterServiceImpl();
        reqConverterService.setPasswordMD5Service(passwordMD5Service);
        UserReq userReq = buildUserReq();
        Mockito.when(passwordMD5Service.codePassword(eq("12345"))).thenReturn("54321");
//        WHEN
        User actualResult = reqConverterService.convert(userReq);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(1L, actualResult.getId().longValue());
        Assert.assertEquals("One", actualResult.getLogin());
        Assert.assertNotNull(actualResult.getRoles());
        Assert.assertEquals("54321", actualResult.getPassword());
    }

    private UserReq buildUserReq() {
        UserReq userReq = new UserReq();
        userReq.setId(1L);
        userReq.setLogin("One");
        userReq.setRoles(new ArrayList<UserRole>());
        userReq.setPassword("12345");
        return userReq;
    }

    @Test
    public void convert4() {
//        GIVEN
        BasketReq basketReq = buildBasketReq();
        ReqConverterServiceImpl reqConverterService = new ReqConverterServiceImpl();
//        WHEN
        Basket actualResult = reqConverterService.convert(basketReq);
//        THEN
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(1L, actualResult.getId().longValue());
        Assert.assertNotNull(basketReq.getUser());
        Assert.assertEquals(BigDecimal.TEN, actualResult.getSum());
        Assert.assertEquals(USD, actualResult.getCurrency());
        Assert.assertNotNull(actualResult.getArticleList());
    }

    private BasketReq buildBasketReq() {
        BasketReq basketReq = new BasketReq();
        basketReq.setId(1L);
        basketReq.setUser(new User());
        basketReq.setSum(BigDecimal.TEN);
        basketReq.setCurrency(USD);
        basketReq.setArticleList(new ArrayList<>());
        return basketReq;
    }
}