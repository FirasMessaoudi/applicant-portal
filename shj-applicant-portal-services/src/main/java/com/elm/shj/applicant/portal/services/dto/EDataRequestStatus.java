/*
 *  Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

/**
 * Enum for data request statuses
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public enum EDataRequestStatus {

    NEW(1), CONFIRMED(2), UNDER_PROCESSING(3), PROCESSED_SUCCESSFULLY(4), PROCESSED_WITH_ERRORS(5), CANCELLED(6);

    private final int id;

    EDataRequestStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
