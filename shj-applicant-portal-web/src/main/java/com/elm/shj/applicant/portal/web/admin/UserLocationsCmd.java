package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dto.UserLocationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * user locations MVC Command
 *
 * @author jaafer jarray
 * @since 1.3.0
 */
@NoArgsConstructor
@Getter
@Setter
public class UserLocationsCmd implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<UserLocationDto> locations;
    private String uin;
}
