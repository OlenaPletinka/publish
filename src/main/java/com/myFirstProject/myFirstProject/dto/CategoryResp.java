package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.Category;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Data
public class CategoryResp {
    private Long id;
    private String name;
}
