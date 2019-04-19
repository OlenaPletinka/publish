package com.myFirstProject.myFirstProject.config;

import lombok.Data;

import java.util.Objects;
@Data
public class ApiErrorResponse {
    private String message;

    public ApiErrorResponse(String message) {
        this.message = message;
    }

}
