package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateContactsCmd implements Serializable {

    private static final long serialVersionUID = 1L;


    @NotNull
    @Length(max = 20, min = 5)
    private String mobileNumber;

    @Length(max = 50, min = 3)
    @Email
    private String email;

    @Length(max = 10)
    private String countryPhonePrefix;
    @Length(max = 10)
    private String countryCode;

    private String pin;

}
