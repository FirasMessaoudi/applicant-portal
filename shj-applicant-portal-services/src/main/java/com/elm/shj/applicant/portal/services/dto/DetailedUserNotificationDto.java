package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailedUserNotificationDto implements Serializable {

    private static final long serialVersionUID = 5803144348731445920L;

    private String resolvedBody;
    private String statusCode;
    private String categoryCode;
    private boolean important;
    private boolean actionRequired;
    private boolean userSpecific;
    private String title;
    private String actionLabel;

}
