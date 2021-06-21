/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.dcc.foundation.commons.validation.NinOrIqama;
import lombok.Data;

import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * Reset Password MVC Command
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Data
public class ResetPasswordCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    @NinOrIqama
    private long idNumber;

    @Past
    private Date dateOfBirthGregorian;

    private int dateOfBirthHijri;

}
