package com.elm.shj.applicant.portal.services.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Dto class for the CompanyRitualSeason.
 *
 * @author Ahmed Elsayed
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class CompanyRitualSeasonLiteDto implements Serializable {

    private static final long serialVersionUID = -5184278952318559368L;

    private long id;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private int seasonStart;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private int seasonEnd;

    @NotNull(message = "validation.data.constraints.msg.20001")
    private RitualSeasonDto ritualSeason;
}
