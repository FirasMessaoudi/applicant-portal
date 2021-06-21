/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract class for any localized database lookup table.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@MappedSuperclass
@Getter
public abstract class JpaLocalizedLookup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "lang", nullable = false)
    private String lang;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
}
