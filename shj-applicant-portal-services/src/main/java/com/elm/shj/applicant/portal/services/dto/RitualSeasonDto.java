package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Dto class for the ritual season domain.
 *
 * @author ahmed elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class RitualSeasonDto implements Serializable {

    private static final long serialVersionUID = -1656233976774218082L;

    private long id;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private int seasonYear;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private String ritualTypeCode;

    private int seasonStart;

    private int seasonEnd;

    private boolean activated;

    @JsonBackReference
    private List<CompanyRitualSeasonLiteDto> companyRitualSeasons;
}
