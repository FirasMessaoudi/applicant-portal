/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_config database table.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_config")
@NamedQuery(name = "JpaConfig.findAll", query = "SELECT j FROM JpaConfig j")
@Data
@NoArgsConstructor
public class JpaConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "CONF_KEY", nullable = false, length = 250)
    private String confKey;

    @Column(name = "CONF_VALUE", nullable = false, length = 250)
    private String confValue;

    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Column(name = "UPDATE_DATE", nullable = false)
    private Date updateDate;

}
