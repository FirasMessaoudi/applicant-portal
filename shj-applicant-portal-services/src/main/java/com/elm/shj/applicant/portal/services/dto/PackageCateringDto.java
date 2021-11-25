package com.elm.shj.applicant.portal.services.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Dto class for the package catering domain.
 *
 * @author ahmed elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class PackageCateringDto implements Serializable {

    private static final long serialVersionUID = 4099330015218595333L;

    private long id;

    private String mealCode;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private Time mealTime;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private String mealDescription;

    private String type;

    private String descriptionAr;

    private String descriptionEn;

    private Date creationDate;

    private Date updateDate;

    private boolean isDefault;

    private PackageHousingDto packageHousing;

    @JsonBackReference(value = "applicantPackageCaterings")
    private List<ApplicantPackageCateringDto> applicantPackageCaterings;

}
