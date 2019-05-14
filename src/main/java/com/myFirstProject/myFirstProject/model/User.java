package com.myFirstProject.myFirstProject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data
public class User {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName="id")}, inverseJoinColumns = {@JoinColumn(name = "role", referencedColumnName= "role")})
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Basket> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DeletedBasket> deletedBaskets = new ArrayList<>();

    // CascadeType.ALL - сделай с владеемыми объектами класса тоже самое, что ты делаешь с владельцем
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Account account;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
