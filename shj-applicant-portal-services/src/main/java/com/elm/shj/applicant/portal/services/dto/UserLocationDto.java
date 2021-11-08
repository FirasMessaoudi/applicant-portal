package com.elm.shj.applicant.portal.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserLocationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private long userId;

    private double latitude;

    private double longitude;

    private double heading;

    private double speed;

    private long timestamp;

    private Date creationDate;
}
