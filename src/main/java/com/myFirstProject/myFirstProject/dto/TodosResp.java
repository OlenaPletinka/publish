package com.myFirstProject.myFirstProject.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class TodosResp {
    private Long userId;
    private Long id;
    private String title;
    private Boolean completed;
}
