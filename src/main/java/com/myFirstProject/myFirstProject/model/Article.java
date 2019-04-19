package com.myFirstProject.myFirstProject.model;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Audited
@Data
public class Article {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private String body;

    //@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) пишемо над обєктами які не позначені @Audited
    //але використовуються в ентіті та що аудітид
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private boolean isPublish = false;

    @Column
    private LocalDateTime timestamp;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToMany(mappedBy = "articleList")
    private List<Basket> baskets = new ArrayList<>();

    // @PrePersist - виконує метод при створенні нового запису в базі
    // @PreUpdate - виконує метод при оновленні
    @PrePersist
    @PreUpdate
    private void onUpsert(){
        this.timestamp = LocalDateTime.now();
    }

    public static class Builder{
        private  Article article;

        public Builder() {
            article = new Article();
        }

        public Builder withId (Long id){
            article.id=id;
            return this;
        }

        public Builder withTitle(String title){
            article.title = title;
            return this;
        }

        public Builder withBody (String body){
            article.body = body;
            return this;
        }

        public Builder withSource(Source source){
            article.source = source;
            return this;
        }

        public Builder withCategory (Category category){
            article.category = category;
            return this;
        }

        public Builder withIsPublish (boolean isPublish){
            article.isPublish = isPublish;
            return this;
        }

        public Builder withBaskets (List<Basket> baskets){
            article.baskets = baskets;
            return this;
        }

        public Article build(){
            return article;
        }
    }
}