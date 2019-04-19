package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.dto.CategoryResp;
import com.myFirstProject.myFirstProject.dto.PaymentResp;
import com.myFirstProject.myFirstProject.dto.SourceResp;
import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.model.Payment;
import com.myFirstProject.myFirstProject.model.Source;

public interface RespConverterService {
    ArticleResp convertToResp(Article article);
    CategoryResp convertCategoryToResp(Category category);
    SourceResp convertSourceToResp (Source source);
    PaymentResp convertPaymentToResp (Payment payment);

}
