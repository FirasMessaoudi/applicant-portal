/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.validation.ArabicCharacters;
import com.elm.dcc.foundation.commons.validation.LatinCharacters;
import com.elm.dcc.foundation.commons.validation.Unique;
import com.elm.shj.applicant.portal.orm.entity.JpaRole;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Dto class for the role domain.
 *
 * @author ahmad flaifel
 * @since 1.8.0
 */
@Getter
@Setter
@NoArgsConstructor
@Data
public class RoleDto implements Serializable {

    private static final long serialVersionUID = 4778905195150470185L;

    private long id;
    @ArabicCharacters(lettersOnly = true, numbersOnly = false)
    @Unique(columnName = "labelAr", entityClass = JpaRole.class, groups = {RoleDto.CreateRoleValidationGroup.class})
    private String labelAr;
    @LatinCharacters(lettersOnly = true, numbersOnly = false)
    @Unique(columnName = "labelEn", entityClass = JpaRole.class, groups = {RoleDto.CreateRoleValidationGroup.class})
    private String labelEn;
    private boolean deleted;
    private boolean activated;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<RoleAuthorityDto> roleAuthorities;
    private Date creationDate;
    private Date updateDate;

    public interface CreateRoleValidationGroup {
    }
}
