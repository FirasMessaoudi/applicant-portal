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
 * The persistent class for the sha_role database table.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Entity
@Table(name = "sha_role")
@NamedQuery(name = "JpaRole.findAll", query = "SELECT j FROM JpaRole j")
@Data
@NoArgsConstructor
public class JpaRole implements Serializable {

    private static final long serialVersionUID = 2532594233381225407L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "label_ar", nullable = false)
    private String labelAr;

    @Column(name = "label_en", nullable = false)
    private String labelEn;

    private boolean deleted;

    private boolean activated;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JpaRoleAuthority> roleAuthorities;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "update_date")
    private Date updateDate;

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = new Date();
    }
}
