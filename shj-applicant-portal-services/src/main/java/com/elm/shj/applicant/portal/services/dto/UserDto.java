/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.validation.Password;
import com.elm.dcc.foundation.commons.validation.SafeFile;
import com.elm.dcc.foundation.commons.validation.Unique;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Dto class for the user domain.
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private Date dateOfBirthGregorian;
    private int dateOfBirthHijri;
    private String email;

    private String fullNameAr;
    private String gender;
    private String fullNameEn;
    private Date lastLoginDate;

    private String mobileNumber;
    private String countryPhonePrefix;
    private String countryCode;
    @Unique(columnName = "uin", entityClass = JpaUser.class, groups = {CreateUserValidationGroup.class})
    private Long uin;
    private Long nin;
    private String passportNumber;
    private String idNumber;

    @SafeFile
    private MultipartFile avatarFile;
    private String avatar;
    private int numberOfTries;
    @Password
    private String password;
    private String passwordHash;
    private String preferredLanguage;
    private Date updateDate;
    private boolean deleted;
    private boolean activated;
    private Date blockDate;
    private boolean blocked;
    private boolean changePasswordRequired;
    private Date creationDate;
    private Set<UserRoleDto> userRoles;
    private Date tokenExpiryDate;
    @Length(max = 20)
    private String nationalityCode;


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UserDto)) {
            return false;
        }
        UserDto user = (UserDto) o;
        return id == user.id && Objects.equals(nin, user.getNin())
                && Objects.equals(passwordHash, user.getPasswordHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nin, mobileNumber);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

    public interface CreateUserValidationGroup {
    }

    public interface UpdateUserValidationGroup {
    }
}
