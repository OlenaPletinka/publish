package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.CategoryReq;
import com.myFirstProject.myFirstProject.dto.CategoryResp;
import com.myFirstProject.myFirstProject.exception.CategoryNotValidForSave;
import com.myFirstProject.myFirstProject.exception.CategoryNotValidForUpdate;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.service.CategoryService;
import com.myFirstProject.myFirstProject.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity saveCategory(@RequestBody CategoryReq categoryReq) throws JsonProcessingException {
        categoryValidForSave(categoryReq);
        Long id = categoryService.saveCategory(categoryReq);
        CategoryResp categoryResp = new CategoryResp();
        categoryResp.setId(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(categoryResp));
    }

    private void categoryValidForSave(CategoryReq categoryReq) {
        if (categoryReq.getId() != null || categoryReq.getName() == null || categoryReq.getName().isEmpty()) {
            throw new CategoryNotValidForSave("Category  is not valid for save");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getById(@PathVariable Long id) throws JsonProcessingException {
        CategoryResp categoryResp = categoryService.getById(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(categoryResp));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteById(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity update(@RequestBody CategoryReq categoryReq) throws JsonProcessingException {
        categoryValidForUpdate(categoryReq);
        CategoryResp updateCategory = categoryService.update(categoryReq);

        return ResponseEntity.ok(objectMapper.writeValueAsString(updateCategory));
    }

    private void categoryValidForUpdate(CategoryReq categoryReq) {
        if (categoryReq.getId() == null || categoryReq.getName() == null || categoryReq.getName().isEmpty()) {
            throw new CategoryNotValidForUpdate(String.format("Category with id %d is not valid for update", categoryReq.getId()));
        }
    }
}
