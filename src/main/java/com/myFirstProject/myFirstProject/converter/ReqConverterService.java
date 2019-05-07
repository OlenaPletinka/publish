package com.myFirstProject.myFirstProject.converter;

import com.myFirstProject.myFirstProject.dto.*;
import com.myFirstProject.myFirstProject.model.*;

import java.security.NoSuchAlgorithmException;

public interface ReqConverterService {
    Article convert (ArticleReq articleReq);
    Category convert(CategoryReq categoryReq);
    Source convert (SourceReq sourceReq);
    User convert (UserReq userReq);
    Basket convert (BasketReq basketReq);
    PromoCode convert(PromoCodeReq promoCodeReq);
}
