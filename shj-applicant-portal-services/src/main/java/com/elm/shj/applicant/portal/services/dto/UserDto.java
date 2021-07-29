/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.validation.*;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
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
    @Past
    private Date dateOfBirthGregorian;
    private int dateOfBirthHijri;
    @Length(max = 50, min = 3)
    @Email
    private String email;

    @NotBlank(groups = {CreateUserValidationGroup.class})
    @Length(max = 100, min = 1)
    @CharactersOnly
    private String fullNameAr;
    private String gender;
    @Length(max = 100, min = 0)
    @CharactersOnly
    private String fullNameEn;
    private Date lastLoginDate;
    @MobileNumber
    private Integer mobileNumber;

    @Unique(columnName = "uin", entityClass = JpaUser.class, groups = {CreateUserValidationGroup.class})
    private Long uin;
    private Long nin;
    @SafeFile
    private MultipartFile avatarFile;
    private String avatar;
    private int numberOfTries;
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
