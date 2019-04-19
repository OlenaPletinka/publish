package com.myFirstProject.myFirstProject.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class TodosReq {
    private LocalDateTime localDateTime = LocalDateTime.now();
}
