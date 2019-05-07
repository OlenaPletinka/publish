package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.*;
import com.myFirstProject.myFirstProject.model.*;
import com.myFirstProject.myFirstProject.service.PasswordMD5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReqConverterServiceImpl implements ReqConverterService {

    private PasswordMD5Service passwordMD5Service;

    @Autowired
    public void setPasswordMD5Service(PasswordMD5Service passwordMD5Service) {
        this.passwordMD5Service = passwordMD5Service;
    }

    @Override
    public Article convert(ArticleReq articleReq) {
        Article article = new Article.Builder()
                .withId(articleReq.getId())
                .withTitle(articleReq.getTitle())
                .withBody(articleReq.getBody())
                .withIsPublish(articleReq.isPublish())
                .build();

        if (articleReq.getSource() != null) {
            Source source = convert(articleReq.getSource());
            article.setSource(source);
        }

        if (articleReq.getCategory() != null) {
            Category category = convert(articleReq.getCategory());
            article.setCategory(category);
        }

        return article;
    }

    @Override
    public Category convert(CategoryReq categoryReq) {
        Category category =  new Category();
        category.setId(categoryReq.getId());
        category.setName(categoryReq.getName());

        return category;
    }

    @Override
    public Source convert(SourceReq sourceReq) {
        Source source = new Source();
        source.setId(sourceReq.getId());
        source.setName(sourceReq.getName());
        return source;
    }

    @Override
    public User convert(UserReq userReq) {
        User user = new User();
        user.setId(userReq.getId());
        user.setLogin(userReq.getLogin());
        String password = userReq.getPassword();
        user.setPassword(passwordMD5Service.codePassword(password));
        user.setRoles(userReq.getRoles());
        return user;
    }

    @Override
    public Basket convert(BasketReq basketReq) {
        Basket basket = new Basket();
        basket.setId(basketReq.getId());
        basket.setUser(basketReq.getUser());
        basket.setSum(basketReq.getSum());
        basket.setCurrency(basketReq.getCurrency());
        basket.setArticleList(basketReq.getArticleList());

        return basket;
    }

    @Override
    public PromoCode convert(PromoCodeReq promoCodeReq) {
        PromoCode promoCode = new PromoCode();
        promoCode.setId(promoCodeReq.getId());
        promoCode.setPromoType(promoCodeReq.getPromoType());
        promoCode.setValue(promoCodeReq.getValue());
        promoCode.setValid(true);
        promoCode.setExpired(promoCodeReq.getExpired());

        return promoCode;
    }
}
