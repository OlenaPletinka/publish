package com.myFirstProject.myFirstProject.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data

public class Category {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Article> articleList = new ArrayList<>();

    @Column
    private LocalDateTime timestamp = LocalDateTime.now();
}
