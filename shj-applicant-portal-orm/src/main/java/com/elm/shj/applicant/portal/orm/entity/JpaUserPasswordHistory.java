/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_user_password_history database table.
 *
 * @author Ahmad Flaifel
 * @since 1.3.0
 */
@Entity
@Table(name = "sha_user_password_history")
@NamedQuery(name = "JpaUserPasswordHistory.findAll", query = "SELECT j FROM JpaUserPasswordHistory j")
@Data
@NoArgsConstructor
public class JpaUserPasswordHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "OLD_PASSWORD_HASH", nullable = false, length = 256)
    private String oldPasswordHash;

    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

}
