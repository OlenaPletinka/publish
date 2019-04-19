package com.myFirstProject.myFirstProject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data
public class Source {

    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    private List<Article> articleList = new ArrayList<>();
}
