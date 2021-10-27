package com.elm.shj.applicant.portal.services.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto for Notifications that will be returned for the user
 *
 * @author Ahmed Ali
 * @since 1.1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailedUserNotificationDto implements Serializable {

    private static final long serialVersionUID = 5803144348731445920L;
    private long id;
    private String resolvedBody;
    private String statusCode;
    private String categoryCode;
    private String nameCode;
    private boolean important;
    private boolean actionRequired;
    private boolean userSpecific;
    private String title;
    private String actionLabel;
    private Date creationDate;
}
