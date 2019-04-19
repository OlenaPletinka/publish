package com.myFirstProject.myFirstProject.model;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table
@Audited
@Data
public class Account {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn
    @Audited(targetAuditMode = NOT_AUDITED)
    private User user;

    @Column
    private BigDecimal sum;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Audited(targetAuditMode = NOT_AUDITED)
    private List<Payment> payments = new ArrayList<>();
}
