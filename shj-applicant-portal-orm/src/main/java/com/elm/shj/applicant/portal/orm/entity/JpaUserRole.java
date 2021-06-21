/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_user_role database table.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_user_role")
@NamedQuery(name = "JpaUserRole.findAll", query = "SELECT j FROM JpaUserRole j")
@Getter
@Setter
@NoArgsConstructor
public class JpaUserRole implements Serializable {

    private static final long serialVersionUID = -711477090284586448L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToOne
    private JpaUser user;

    @ManyToOne
    private JpaRole role;

    @Column(name = "is_main_role")
    private boolean mainRole;

    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
    }
}
