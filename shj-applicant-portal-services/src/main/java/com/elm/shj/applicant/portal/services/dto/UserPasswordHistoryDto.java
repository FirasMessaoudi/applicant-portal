/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Dto class for the user password history .
 *
 * @author Ahmad Flaifel
 * @since 1.3.0
 */
@Getter
@Setter
@NoArgsConstructor
public class UserPasswordHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    @JsonBackReference
    private long userId;
    private String oldPasswordHash;
    private Date creationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPasswordHistoryDto that = (UserPasswordHistoryDto) o;
        return id == that.id && userId == that.userId &&
                Objects.equals(oldPasswordHash, that.oldPasswordHash) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, oldPasswordHash, creationDate);
    }
}
