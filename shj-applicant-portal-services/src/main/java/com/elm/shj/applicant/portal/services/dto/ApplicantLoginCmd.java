package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicantLoginCmd implements Serializable {

    private static final long serialVersionUID = 6641483817347109390L;
    private String type;
    private String nationalityCode;
    private String idNumber;
    private String password;
}
