package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Category;
import com.myFirstProject.myFirstProject.model.Source;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ArticleReq {
    private Long id;
    private String title;
    private String body;
    private List<String> images = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private SourceReq source;
    private CategoryReq category;
    private boolean isPublish;
}
