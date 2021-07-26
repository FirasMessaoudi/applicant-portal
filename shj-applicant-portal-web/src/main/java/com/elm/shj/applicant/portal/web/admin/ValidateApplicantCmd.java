package com.elm.shj.applicant.portal.web.admin;

import lombok.Data;

import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

@Data
public class ValidateApplicantCmd implements Serializable {

    private static final long serialVersionUID = 6641483817347109390L;

    private String uin;


    private String dateOfBirthGregorian;

    private int dateOfBirthHijri;

}
