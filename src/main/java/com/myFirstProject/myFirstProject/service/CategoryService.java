package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.ArticleReq;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.dto.CategoryReq;
import com.myFirstProject.myFirstProject.dto.CategoryResp;

public interface CategoryService {
    Long saveCategory(CategoryReq categoryReq);
    CategoryResp getById(Long id);
    void delete(Long id);
    CategoryResp update(CategoryReq categoryReq);
}
