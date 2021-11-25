/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Dto class for the companyStaff.
 *
 * @author salzoubi
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class CompanyStaffDto {

    private static final long serialVersionUID = -2973750038306749840L;
    private long id;

    private String fullNameAr;

    private String fullNameEn;

    @Min(1)
    @Max(15)
    @NotNull(message = "validation.data.constraints.msg.20001")
    private int idNumber;

   /* @NotNull(message = "validation.data.constraints.msg.20001")
    @JsonBackReference(value = "company")
    private CompanyDto company;

    @JsonBackReference(value = "applicantGroups")
    private List<ApplicantGroupDto> applicantGroups;*/

    private String titleCode;

    @Max(20)
    @Min(10)
    @NotNull(message = "validation.data.constraints.msg.20001")
    private String mobileNumber;

    @Email(message = "validation.data.constraints.msg.20003")
    private String email;

    private Date creationDate;
    private Date updateDate;
}
