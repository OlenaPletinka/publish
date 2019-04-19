package com.myFirstProject.myFirstProject.model;

import com.myFirstProject.myFirstProject.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Data
public class UserRole {

    @Column
    @Id
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "roles")
    private List<User> userRoles = new ArrayList<>();
}
