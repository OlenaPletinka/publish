package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterServiceImpl;
import com.myFirstProject.myFirstProject.converter.RespConverterServiceImpl;
import com.myFirstProject.myFirstProject.dto.CategoryReq;
import com.myFirstProject.myFirstProject.dto.CategoryResp;
import com.myFirstProject.myFirstProject.exception.CategoryNotExistException;
import com.myFirstProject.myFirstProject.exception.CategoryNotFoundException;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private ReqConverterServiceImpl categoryReqConverterService;

    @Autowired
    private RespConverterServiceImpl categoryRespConverterService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Long saveCategory(CategoryReq categoryReq) {
        logger.info("Get category to update {}", categoryReq);
        Category category = categoryReqConverterService.convert(categoryReq);
        categoryRepository.save(category);
        Long id = category.getId();
        logger.info("Category saved with id {}", id);

        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResp getById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(category1 -> {
            CategoryResp categoryResp = categoryRespConverterService.convertCategoryToResp(category.get());
            return categoryResp;
        }).orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id %d is not found", id)));
//        if (category.isPresent()) {
//            return categoryRespConverterService.convertCategoryToResp(category.get());
//        } else {
//            throw new CategoryNotFoundException(String.format("Category with id %d is not found", id));
//        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (categoryRepository.existsById(id)) {
            logger.info("Delete category with id {}", id);
            categoryRepository.deleteById(id);
            logger.info("Category with id {} was delete ", id);
        }

    }

    @Transactional
    @Override
    public CategoryResp update(CategoryReq categoryReq) {
        if (categoryRepository.existsById(categoryReq.getId())) {
            logger.info("Update category{}", categoryReq);
            Category category = categoryReqConverterService.convert(categoryReq);
            Category savedCategory = categoryRepository.save(category);
            Long id = savedCategory.getId();
            logger.info("Category updated with id {}", id);

            return categoryRespConverterService.convertCategoryToResp(savedCategory);
        } else {
            throw new CategoryNotExistException(String.format("Category with id %d don't exist", categoryReq.getId()));
        }
    }
}

