/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the sha_authority_lk database table.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Entity
@Table(name = "sha_authority_lk")
@NamedQuery(name = "JpaAuthorityLookup.findAll", query = "SELECT j FROM JpaAuthorityLookup j")
@Data
@NoArgsConstructor
public class JpaAuthorityLookup implements Serializable {

    private static final long serialVersionUID = 2292372553652227103L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "label_ar", nullable = false)
    private String labelAr;

    @Column(name = "label_en", nullable = false)
    private String labelEn;

    private String code;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private JpaAuthorityLookup parent;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<JpaAuthorityLookup> children;
}
