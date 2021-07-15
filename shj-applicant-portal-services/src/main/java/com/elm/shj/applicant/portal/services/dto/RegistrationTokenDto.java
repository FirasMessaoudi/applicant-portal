package com.elm.shj.applicant.portal.services.dto;

import com.elm.dcc.foundation.commons.validation.*;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@NoArgsConstructor
@Data
public class RegistrationTokenDto implements Serializable {
    private static final long serialVersionUID = -7583122536438862246L;

    private long id;
    @Past
    private Date dateOfBirthGregorian;
    private int dateOfBirthHijri;
    private String token;
    @NinOrIqama
    private Long nin;
    private long uin;
    private boolean deleted;
    private Date creationDate;
    private Date tokenExpirationDate;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UserDto)) {
            return false;
        }
        RegistrationTokenDto registrationToken  = (RegistrationTokenDto) o;
        return id == registrationToken.id && Objects.equals(uin, registrationToken.getUin()) && Objects.equals(dateOfBirthGregorian, registrationToken.getDateOfBirthGregorian());

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uin, dateOfBirthGregorian);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }


}
