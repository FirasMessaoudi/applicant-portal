package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto class for the applicant health immunization lite domain.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
public class ApplicantHealthImmunizationLiteDto implements Serializable {

    private static final long serialVersionUID = -5478275049682327662L;

    private String immunizationCode;
    private Date immunizationDate;
    private boolean mandatory;
}
