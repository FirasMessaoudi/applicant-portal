/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Update Applicant MVC Command
 *
 * @author Slim Ben Hadj
 * @since 1.1.0
 */
@Data
@AllArgsConstructor
public class UpdateApplicantCmd implements Serializable {

    private static final long serialVersionUID = -7437270089118787394L;

    private String uin;

    @NotBlank(groups = {UserDto.CreateUserValidationGroup.class})
    @Length(max = 50, min = 5)
    @Email(message = "validation.data.constraints.msg.20003")
    private String email;

    @NotBlank(groups = {UserDto.CreateUserValidationGroup.class})
    @Length(max = 16, min = 5)
    private String mobileNumber;


    @Override
    public String toString() {
        return '{' + "\"uin\":" + "\"" + uin + "\"" + ",\"email\":" + "\"" + email + "\"" + ",\"mobileNumber\":" + "\"" + mobileNumber + "\"" + '}';
    }
}