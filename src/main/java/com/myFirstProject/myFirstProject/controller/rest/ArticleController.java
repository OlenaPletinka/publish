package com.myFirstProject.myFirstProject.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myFirstProject.myFirstProject.dto.ArticleReq;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.exception.ArticleNotValidForCreateException;
import com.myFirstProject.myFirstProject.exception.ArticleNotValidForUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myFirstProject.myFirstProject.service.ArticleService;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping(path = "/article/")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity saveArticle(@RequestBody ArticleReq article) throws JsonProcessingException {
        validForCreate(article);
        Long id = articleService.saveArticle(article);

        return ResponseEntity.ok(objectMapper.writeValueAsString(new ArticleResp(id)));

    }

    private boolean isValid(ArticleReq article) {
        return article != null && !isBlank(article.getBody()) && !isBlank(article.getTitle()) && article.getSource()!= null && article.getCategory()!=null;
    }

    private boolean isBlank(String text) {
        return text == null || text.isEmpty();

    }

    private void validForUpdate(ArticleReq articleReq) {
        if (!isValid(articleReq) || articleReq.getId() == null) {
            throw new ArticleNotValidForUpdateException(String.format("Article with id %d is not valid for update", articleReq.getId()));
        }
    }

    private void validForCreate(ArticleReq articleReq) {
        if (!isValid(articleReq) || articleReq.getId() != null) {
            throw new ArticleNotValidForCreateException("Article is not valid for creation");
        }
    }

    //@PathVariable - це точний ідентифікатор пошуку
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getById(@PathVariable Long id) throws JsonProcessingException {
        ArticleResp articleResp = articleService.getById(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(articleResp));
    }


    //@RequestParam - це параметри (...=...) за якими здійснюється пошук
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findByParam(@RequestParam (name = "title", required = false) String title,@RequestParam(name = "source", required = false)String source, @RequestParam (name = "name", required = false) String name ) throws JsonProcessingException {
        List<ArticleResp> articleFindByTitleAndSource = articleService.findByParam(title, source, name);

        return ResponseEntity.ok(objectMapper.writeValueAsString(articleFindByTitleAndSource));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable Long id) throws JsonProcessingException {
        articleService.delete(id);

        return new ResponseEntity(HttpStatus.ACCEPTED);

    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity updateArticle(@RequestBody ArticleReq article) throws JsonProcessingException {
        validForUpdate(article);
        ArticleResp updateArticle = articleService.update(article);

        return ResponseEntity.ok(objectMapper.writeValueAsString(updateArticle));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json" )
    public ResponseEntity publish(@PathVariable Long id) throws JsonProcessingException {
        ArticleResp updateArticle = articleService.publish(id);

        return ResponseEntity.ok(objectMapper.writeValueAsString(updateArticle));
    }

    //PUT тому що GET не приймає баді, щоб передати реквест
    @RequestMapping(path =  "search/",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity search (@RequestBody ArticleReq articleReq) throws JsonProcessingException {
        List<ArticleResp> respList = articleService.search(articleReq);

        return ResponseEntity.ok(objectMapper.writeValueAsString(respList));
    }

}
