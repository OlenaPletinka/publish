package com.myFirstProject.myFirstProject.dto;

import com.myFirstProject.myFirstProject.model.UserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class UserReq {
    private Long id;
    private String login;
    private String password;
    private List<UserRole> roles = new ArrayList<>();
}
