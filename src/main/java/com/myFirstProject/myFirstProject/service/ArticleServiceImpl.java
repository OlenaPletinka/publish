package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.converter.ReqConverterService;
import com.myFirstProject.myFirstProject.converter.RespConverterService;
import com.myFirstProject.myFirstProject.dto.ArticleReq;
import com.myFirstProject.myFirstProject.dto.ArticleResp;
import com.myFirstProject.myFirstProject.exception.ArticleIsNotExistException;
import com.myFirstProject.myFirstProject.exception.ArticleNotFoundException;
import com.myFirstProject.myFirstProject.exception.CategoryNotExistException;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.Article;
import com.myFirstProject.myFirstProject.model.Basket;
import com.myFirstProject.myFirstProject.model.User;
import com.myFirstProject.myFirstProject.repository.ArticleRepository;
import com.myFirstProject.myFirstProject.repository.CategoryRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private ReqConverterService reqConverterService;
    private RespConverterService respConverterService;
    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public void setReqConverterService(ReqConverterService reqConverterService) {
        this.reqConverterService = reqConverterService;
    }

    @Autowired
    public void setRespConverterService(RespConverterService respConverterService) {
        this.respConverterService = respConverterService;
    }

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Long saveArticle(ArticleReq articleReq) {
        logger.info("Get article to update {}", articleReq);
        Article article = reqConverterService.convert(articleReq);
        if (categoryRepository.existsById(article.getCategory().getId())) {
            articleRepository.save(article);
            Long id = article.getId();
            logger.info("Article saved with id {}", id);

            return id;
        } else {
            throw new CategoryNotExistException(String.format("Article can not be created, because category with id %d not exist", article.getCategory().getId()));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResp getById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        //map перетвор optional в необхідний обєкт
        return article.map(article1 -> {
            ArticleResp articleResp = respConverterService.convertToResp(article.get());
            return articleResp;
        }).orElseThrow(() ->
                new ArticleNotFoundException(String.format("Article with id %d not found", id))
        );
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (articleRepository.existsById(id)) {
            logger.info("Delete article with id {}", id);
            articleRepository.deleteById(id);
            logger.info("Article with id {} was delete", id);
        }
    }

    @Transactional
    @Override
    public ArticleResp update(ArticleReq articleReq) {
        if (articleRepository.existsById(articleReq.getId())) {
            logger.info("Update article {}", articleReq);
            Article article = reqConverterService.convert(articleReq);
            Article savedArticle = articleRepository.save(article);
            Long id = savedArticle.getId();
            logger.info("Article update with id {}", id);

            return respConverterService.convertToResp(savedArticle);
        } else {
            throw new ArticleIsNotExistException(String.format("Article with id %s not found", articleReq.getId().toString()));
        }

    }

    @Transactional
    @Override
    public ArticleResp publish(Long id) {
        logger.info("Update publish state of article with id {}", id);
        Optional<Article> articleFromRepository = articleRepository.findById(id);
        return articleFromRepository.map(article -> {
            article.setPublish(true);
            ArticleResp articleResp = respConverterService.convertToResp(article);
            logger.info("Publish state of article with id {} was update", id);

            return articleResp;
        }).orElseThrow(() -> new ArticleNotFoundException(String.format("Article with id %d not found", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> findByParam(String title, String source, String name) {

        if (title != null && source != null && name != null) {

            return findByTitleSourceName(title, source, name);
        } else if (title != null && source != null && name == null) {

            return findByTitleAndSource(title, source);
        } else if (title != null && source == null && name == null) {

            return findByTitle(title);
        } else {

            return findAllArticlesWithSameCategoryName(name);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> findByTitleSourceName(String title, String source, String name) {
        List<Article> articlesfindByTitleSourceName = articleRepository.findByTitleSourceName(title, source, name);
        logger.info(String.format("%d articles was found by title and name of the source", articlesfindByTitleSourceName.size()));
        List<ArticleResp> articlesRespfindByTitleSourceName = new ArrayList<>();
        articlesfindByTitleSourceName.stream().forEach(article -> {
            articlesRespfindByTitleSourceName.add(respConverterService.convertToResp(article));
        });

        return articlesRespfindByTitleSourceName;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> findByTitle(String title) {
        List<Article> articleFindByTitle = articleRepository.findByTitle(title);
        logger.info(String.format("%d articles was found by title", articleFindByTitle.size()));
        List<ArticleResp> articleRespFindByTitle = new ArrayList<>();
        articleFindByTitle.stream().forEach(article -> {
            articleRespFindByTitle.add(respConverterService.convertToResp(article));
        });

        return articleRespFindByTitle;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> findAllArticlesWithSameCategoryName(String name) {
        List<Article> articleWithSameCategory = articleRepository.findAllArticleWithSameCategoryName(name);
        logger.info(String.format("%d articles was found by same name of the source", articleWithSameCategory.size()));
        List<ArticleResp> articleRespWithSameCategory = new ArrayList<>();
        articleWithSameCategory.stream().forEach(article -> articleRespWithSameCategory.add(respConverterService.convertToResp(article)));

        return articleRespWithSameCategory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> findByTitleAndSource(String title, String source) {
        List<Article> articleFindByTitleAndSource = articleRepository.findByTitleAndSource(title, source);
        logger.info(String.format("%d articles was found by title and source", articleFindByTitleAndSource.size()));
        List<ArticleResp> articleRespFindByTitleAndSource = new ArrayList<>();
        articleFindByTitleAndSource.stream().forEach(article -> articleRespFindByTitleAndSource.add(respConverterService.convertToResp(article)));

        return articleRespFindByTitleAndSource;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Long> findAllArticlesIdWithSameCategoryName(String name) {
        List<Long> articlesIdWithSameCategory = articleRepository.findAllArticlesIdWithSameCategoryName(name);
        logger.info(String.format("%d articles was found with the same category", articlesIdWithSameCategory.size()));

        return articlesIdWithSameCategory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> search(ArticleReq articleReq) {
        Article article = reqConverterService.convert(articleReq);
        //використовуємо екземпляр для того щоб не створювати міліон рестів

        Example<Article> example = Example.of(article);
        Iterable<Article> articles = articleRepository.findAll(example);
        List<Article> allArticles = new ArrayList<>();
        articles.forEach(allArticles::add);

        List<ArticleResp> respList = new ArrayList<>();
        allArticles.stream().forEach(article1 -> respList.add(respConverterService.convertToResp(article1)));

        return respList;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleResp> getArticlesFromBasketByUserId(Long id) {
        List<ArticleResp> articleResps = new ArrayList<>();
        Optional<User> optional = userRepository.findById(id);
        return optional.map(user -> {
            logger.info(String.format("Find user with id %d", id));
            List<Basket> orders = user.getOrders();
            logger.info(String.format("User with id %d made %d orders", id, orders.size()));
            orders.stream().forEach(basket -> basket.getArticleList().stream()
                            .forEach(article -> articleResps.add(respConverterService.convertToResp(article)))
            );
            logger.info(String.format("User with id %d made %d orders witch contain %d articles", id, orders.size(), articleResps.size()));
            return articleResps;
        }).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id %d not found", id))
        );
    }


}

