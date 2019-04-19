package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.dto.UserReq;
import com.myFirstProject.myFirstProject.dto.UserResp;
import com.myFirstProject.myFirstProject.exception.UserNotValidForSaveException;
import com.myFirstProject.myFirstProject.service.ArticleService;
import com.myFirstProject.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping(path = "/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping( method = RequestMethod.POST, produces =  "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveUser (@RequestBody UserReq userReq) throws JsonProcessingException{
        userValidForSave(userReq);
        Long id = userService.saveUser(userReq);
        UserResp userResp = new UserResp();
        userResp.setId(id);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void userValidForSave(UserReq userReq) {
        if (userReq.getId()!=null||userReq.getLogin()==null||userReq.getPassword()==null){
            throw new UserNotValidForSaveException("User is not valid for save");
        }
    }

    @RequestMapping (value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getArticlesFromBasketByUserId (@PathVariable Long id) throws JsonProcessingException {
        List<ArticleResp> articlesFromBasketByUserId = articleService.getArticlesFromBasketByUserId(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(articlesFromBasketByUserId));


    }
}
