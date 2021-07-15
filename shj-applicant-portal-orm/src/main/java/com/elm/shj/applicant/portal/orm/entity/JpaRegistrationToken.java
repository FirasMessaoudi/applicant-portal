package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "sha_registration_token")
@NamedQuery(name = "JpaRegistrationToken.findAll", query = "SELECT r FROM JpaRegistrationToken r")
@Data
@NoArgsConstructor
public class JpaRegistrationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;
    private String token;
    @Column(name = "TOKEN_EXPIRATION_DATE")
    private Date tokenExpirationDate;

    private long nin;
    private long uin;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH_GREGORIAN")
    private Date dateOfBirthGregorian;
    @Column(name = "DATE_OF_BIRTH_HIJRI")
    private int dateOfBirthHijri;

    private boolean deleted;

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
