package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the applicant health basic domain.
 *
 * @author f.messaoudi
 * @since 1.3.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthBasicDto implements Serializable {

    private static final long serialVersionUID = -6152719368063316734L;

    private long id;
    private String bloodType;
    private ApplicantBasicDto applicant;
    private List<ApplicantHealthDiseaseBasicDto> diseases;
    private List<ApplicantHealthSpecialNeedsBasicDto> specialNeeds;
    private List<ApplicantHealthImmunizationBasicDto> immunizations;
    private Boolean hasSpecialNeeds;
    private String insurancePolicyNumber;
    private ApplicantRitualBasicDto applicantRitual;
    private String packageReferenceNumber;

}
