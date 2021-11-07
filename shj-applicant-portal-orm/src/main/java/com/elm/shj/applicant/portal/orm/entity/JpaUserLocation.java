package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sha_user_location")
@NamedQuery(name = "JpaUserLocation.findAll", query = "SELECT j FROM JpaUserLocation j")
@Data
@NoArgsConstructor
public class JpaUserLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private JpaUser user;

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
