package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.Category;
import lombok.Data;

import java.util.Objects;
@Data
public class ArticleResp {
    private Long id;
    private String title;
    private String body;
    private SourceResp source;
    private CategoryResp category;
    private boolean isPublish;

    public ArticleResp(Long id) {
        this.id = id;
    }
}
