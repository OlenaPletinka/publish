package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.dto.CategoryResp;
import com.myFirstProject.myFirstProject.dto.PaymentResp;
import com.myFirstProject.myFirstProject.dto.SourceResp;
import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.model.Payment;
import com.myFirstProject.myFirstProject.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespConverterServiceImpl implements RespConverterService {

    @Override
    public ArticleResp convertToResp(Article article) {
        ArticleResp articleResp = new ArticleResp(article.getId());
        articleResp.setTitle(article.getTitle());
        articleResp.setBody(article.getBody());

        SourceResp sourceResp = convertSourceToResp(article.getSource());
        articleResp.setSource(sourceResp);

        CategoryResp categoryResp = convertCategoryToResp(article.getCategory());
        articleResp.setCategory(categoryResp);
        articleResp.setPublish(article.isPublish());

        return articleResp;
    }

    @Override
    public CategoryResp convertCategoryToResp(Category category) {
        CategoryResp categoryResp = new CategoryResp();
        categoryResp.setId(category.getId());
        categoryResp.setName(category.getName());
        return categoryResp;
    }

    @Override
    public SourceResp convertSourceToResp(Source source) {
        SourceResp sourceResp = new SourceResp();
        sourceResp.setId(source.getId());
        sourceResp.setName(source.getName());
        return sourceResp;
    }

    @Override
    public PaymentResp convertPaymentToResp(Payment payment) {
        PaymentResp paymentResp = new PaymentResp();
        paymentResp.setId(payment.getId());
        paymentResp.setCurrencyEntity(payment.getCurrencyEntity());
        paymentResp.setPaymentSystemEntity(payment.getPaymentSystemEntity());
        paymentResp.setRate(payment.getRate());
        paymentResp.setSum(payment.getSum());
        paymentResp.setTimeOfPayment(payment.getTimeOfPayment());
        return paymentResp;
    }


}
