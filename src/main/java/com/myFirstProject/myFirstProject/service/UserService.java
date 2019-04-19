package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.UserReq;
import com.myFirstProject.myFirstProject.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserByLoginAndPassword(String login, String password);
    Long saveUser(UserReq userReq);
}