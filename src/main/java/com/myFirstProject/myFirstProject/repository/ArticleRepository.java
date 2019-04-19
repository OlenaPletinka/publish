package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long>, QueryByExampleExecutor<Article> {

    List<Article> findByTitleAndSource(String title, String source);

    @Query("SELECT a FROM Article a WHERE a.title = :title")
    List<Article> findByTitle (@Param("title") String title);

    @Query("SELECT a FROM Article a WHERE a.title = :title AND a.source = :source AND a.category IN (SELECT c.id FROM Category c WHERE c.name = :name)")
    List<Article> findByTitleSourceName(@Param("title") String title, @Param("source") String source, @Param("name") String name);

    @Query("SELECT a.id FROM Article a LEFT JOIN Category c ON a.category = c.id WHERE c.name = :name")
    List<Long> findAllArticlesIdWithSameCategoryName(@Param("name") String name);

    @Query("SELECT a FROM Article a WHERE a.category IN (SELECT c.id FROM Category c WHERE c.name = :name)")
    List<Article> findAllArticleWithSameCategoryName(@Param("name") String name);

    List<Article> findByTimestampLessThanAndIsPublishIs (LocalDateTime timestamp, boolean isPublish);
}
