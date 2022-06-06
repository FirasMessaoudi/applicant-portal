package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidateApplicantCmd implements Serializable {

    private static final long serialVersionUID = 6641483817347109390L;
    private String type;
    private String identifier;
    private String nationalityCode;
    private String dateOfBirthGregorian;
    private int dateOfBirthHijri;

    @Override
    public String toString() {
        return '{' + "\"type\":" + type + "\"uin\":" + "\"" + identifier + "\"" + ",\"dateOfBirthGregorian\":" + "\"" + dateOfBirthGregorian + "\"" + ",\"dateOfBirthHijri\":" + "\"" + dateOfBirthHijri + "\"" + '}';
    }
}
