package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidateApplicantCmd implements Serializable {

    private static final long serialVersionUID = 6641483817347109390L;
    private String uin;
    private String dateOfBirthGregorian;
    private int dateOfBirthHijri;

    @Override
    public String toString() {
        return '{' + "\"uin\":" + "\"" + uin + "\"" + ",\"dateOfBirthGregorian\":" + "\"" + dateOfBirthGregorian + "\"" + ",\"dateOfBirthHijri\":" + "\"" + dateOfBirthHijri + "\"" + '}';
    }
}
