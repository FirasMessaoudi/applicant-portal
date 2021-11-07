package com.elm.shj.applicant.portal.web.admin;

import com.elm.shj.applicant.portal.services.dto.UserLocationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class UserLocationsCmd implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<UserLocationDto> locations;
    private String uin;
}
