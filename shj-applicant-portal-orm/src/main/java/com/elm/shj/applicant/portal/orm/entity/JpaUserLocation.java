/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the sha_user_location database table.
 *
 * @author jaafer jarray
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_user_location")
@NamedQuery(name = "JpaUserLocation.findAll", query = "SELECT j FROM JpaUserLocation j")
@Data
@NoArgsConstructor
public class JpaUserLocation implements Serializable {


    private static final long serialVersionUID = 2766666271092315066L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "altitude")
    private double altitude;

    @Column(name = "heading")
    private double heading;

    @Column(name = "speed")
    private double speed;

    @Column(name = "gps_time")
    private Date time;

    @Transient
    private long timestamp;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "update_date")
    private Date updateDate;

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
        time = new Date(timestamp);
    }

    @PreUpdate
    public void preUpdate() {
        updateDate = new Date();
    }


}
