package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant health immunization lite domain.
 *
 * @author f.messaoudi
 * @since 1.3.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthImmunizationBasicDto implements Serializable {

    private static final long serialVersionUID = -5478275049682327662L;
    private long id;
    private String immunizationCode;
    private Date immunizationDate;
    private boolean mandatory;
    @JsonBackReference
    private ApplicantHealthBasicDto applicantHealth;

}
