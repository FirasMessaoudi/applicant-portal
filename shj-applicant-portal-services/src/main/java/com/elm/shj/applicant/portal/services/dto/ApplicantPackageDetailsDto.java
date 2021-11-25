package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the applicant package details domain.
 *
 * @author ahmed elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class ApplicantPackageDetailsDto implements Serializable {

    private static final long serialVersionUID = 52707732635351247L;

    private List<ApplicantPackageCateringDto> applicantPackageCaterings;
    private List<ApplicantPackageHousingDto> applicantPackageHousings;
    private List<ApplicantPackageTransportationDto> applicantPackageTransportations;
    private CompanyLiteDto companyLite;
}
