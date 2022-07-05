package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the applicant domain.
 *
 * @author ahmad flaifel
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplicantBasicDto implements Serializable {

    private static final long serialVersionUID = 4276580006724069703L;

    private long id;
    private String gender;
    private String idNumber;
    private String passportNumber;
    private String nationalityCode;
    private Date dateOfBirthGregorian;
    private Long dateOfBirthHijri;
    private String packageReferenceNumber;
    private List<ApplicantDigitalIdDto> digitalIds;
    private String countryPhonePrefix;
    private String fullNameAr;
    private String fullNameEn;
    private String fullNameOrigin;
    private String preferredLanguage;
    private String photo;
    private String email;
    private boolean hasLocalMobileNumber;
    private String mobileNumber;
    private String countryCode;
    private boolean deleted;
    private String companyCode;
    private Integer establishmentRefCode;
    private Integer missionRefCode;
    private Long serviceGroupMakkahCode;
    private Long serviceGroupMadinaCode;
    private String idNumberOriginal;
    private String maritalStatusCode;
    private String biometricDataFinger;
    private String biometricDataFace;
    private String educationLevelCode;
    private boolean registered;
    private Date updateDate;
    private Date creationDate;

}
