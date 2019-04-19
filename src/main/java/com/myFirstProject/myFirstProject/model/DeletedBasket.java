package com.myFirstProject.myFirstProject.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@Data
public class DeletedBasket {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private LocalDateTime expiredTime;

    @Column
    private Long idDeletedBasket;

    @Column
    private BigDecimal sum;
}
