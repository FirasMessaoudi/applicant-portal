/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.admin;

import com.elm.dcc.foundation.commons.validation.FieldMatch;
import com.elm.shj.applicant.portal.services.data.validators.HajjPassword;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Change Password MVC Command
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@FieldMatch(first = "newPassword", second = "newPasswordConfirm")
public class ChangePasswordCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String oldPassword;
    @HajjPassword
    private String newPassword;
    private String newPasswordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }


}
