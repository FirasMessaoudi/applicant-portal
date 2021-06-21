/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_role_authority database table.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Entity
@Table(name = "sha_role_authority")
@NamedQuery(name = "JpaRoleAuthority.findAll", query = "SELECT j FROM JpaRoleAuthority j")
@Data
@NoArgsConstructor
public class JpaRoleAuthority implements Serializable {

    private static final long serialVersionUID = -6656022888548145405L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private JpaRole role;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private JpaAuthorityLookup authority;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
}
