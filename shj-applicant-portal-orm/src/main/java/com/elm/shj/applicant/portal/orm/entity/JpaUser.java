/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the sha_user database table.
 * 
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Entity
@Table(name = "sha_user")
@NamedQuery(name = "JpaUser.findAll", query = "SELECT j FROM JpaUser j")
@Getter
@Setter
@NoArgsConstructor
public class JpaUser implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private long id;

	private boolean activated;

	@Column(name = "BLOCK_DATE")
	private Date blockDate;

	private boolean blocked;

	@Column(name = "CHANGE_PASSWORD_REQUIRED")
	private boolean changePasswordRequired;

	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_BIRTH_GREGORIAN")
	private Date dateOfBirthGregorian;

	@Column(name = "DATE_OF_BIRTH_HIJRI")
	private int dateOfBirthHijri;

	private boolean deleted;

    @Column(length = 255)
    private String email;

    @Column(name = "full_name_en", length = 150)
    private String fullNameEn;

    @Column(name = "full_name_ar", length = 150)
    private String fullNameAr;

    @Column(length = 1)
    private String gender;

    @Column(name = "LAST_LOGIN_DATE")
    private Date lastLoginDate;
    @Column(name = "COUNTRY_CODE")
    private String countryCode;
	@Column(name = "country_phone_prefix")
	private String countryPhonePrefix;
	@Column(name = "MOBILE_NUMBER", nullable = false)
	private String mobileNumber;

	private long nin;

	@Column(nullable = false)
	private long uin;

	@Column(name = "passport_number")
	private String passportNumber;

	@Column(name = "id_number")
	private String idNumber;

	@Column(name = "NUMBER_OF_TRIES")
	private int numberOfTries;

	@Column(name = "PASSWORD_HASH", nullable = false, length = 256)
	private String passwordHash;

	@Column(name = "PREFERRED_LANGUAGE", length = 2)
	private String preferredLanguage;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "ACTION_DATE")
	private Date actionDate;

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user")
	private Set<JpaUserRole> userRoles;

	@Column(name = "AVATAR")
	private String avatar;

	@Column(name = "TOKEN_EXPIRY_DATE")
	private Date tokenExpiryDate;

	@PrePersist
	public void prePersist() {
		creationDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		updateDate = new Date();
	}
}
