package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Dto class for the company.
 *
 * @author Ahmed Elsayed
 * @since 1.1.0
 */

@NoArgsConstructor
@Data
public class CompanyLiteDto implements Serializable {

    private static final long serialVersionUID = -2782740641877406749L;
    private long id;
    private String code;

    private String labelAr;

    private String labelEn;

    private String contactNumber;

    private double locationLat;

    private double locationLng;
}
