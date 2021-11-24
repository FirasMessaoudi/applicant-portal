package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the applicant main data domain.
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantMainDataDto implements Serializable {

    private static final long serialVersionUID = -211394632324992513L;
    private long id;
    private String gender;
    private String nationalityCode;
    private String idNumber;
    private String idNumberOriginal;
    private String passportNumber;
    private Date dateOfBirthGregorian;
    private Long dateOfBirthHijri;
    private String fullNameAr;
    private String fullNameEn;
    private String fullNameOrigin;
    private String maritalStatusCode;
    private String photo;
    private List<ApplicantRelativeDto> relatives;
    private List<ApplicantContactDto> contacts;
    private String statusCode;
    private String ritualTypeCode;
    private String cardReferenceNumber;
    private String cardStatusCode;
    private String uin;
}
