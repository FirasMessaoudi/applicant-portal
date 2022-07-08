package com.elm.shj.applicant.portal.orm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sha_otp_cache")
@NamedQuery(name = "JpaOtpCache.findAll", query = "SELECT j FROM JpaOtpCache j")
@Data
@NoArgsConstructor
public class JpaOtpCache implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private long id;
    private String principle;
    private String otp;
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;
}
