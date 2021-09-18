/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the Housing Zone .
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousingZoneDto implements Serializable {


    private static final long serialVersionUID = 7575765144322740810L;

    private long id;
    private String nameAr;
    private String nameEn;
    private String color;
    @JsonBackReference("packageHousings")
    private List<PackageHousingDto> packageHousings;

}
