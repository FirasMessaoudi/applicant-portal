package com.elm.shj.applicant.portal.services.user;

import com.elm.dcc.foundation.providers.recaptcha.exception.RecaptchaException;
import com.elm.shj.applicant.portal.orm.entity.JpaRegistrationToken;
import com.elm.shj.applicant.portal.orm.entity.JpaUser;
import com.elm.shj.applicant.portal.orm.repository.RegistrationTokenRepository;
import com.elm.shj.applicant.portal.services.dto.RegistrationTokenDto;
import com.elm.shj.applicant.portal.services.dto.UserDto;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RegistrationTokenService extends GenericService<JpaRegistrationToken, RegistrationTokenDto, Long> {
    private final RegistrationTokenRepository registrationTokenRepository;


    public RegistrationTokenDto createRegistrationTokenForUser(UserDto user) {

        JpaRegistrationToken existRegistToken = registrationTokenRepository.findByUinAndDeletedFalse(user.getNin());

        if (existRegistToken != null && existRegistToken.getToken() !=null) {
            return  getMapper().fromEntity(existRegistToken, mappingContext);
        }
        RegistrationTokenDto registrationToken=new RegistrationTokenDto();
        registrationToken.setDateOfBirthGregorian(user.getDateOfBirthGregorian());
        registrationToken.setDateOfBirthHijri(user.getDateOfBirthHijri());
        registrationToken.setNin(user.getNin());
        //change this to uin
        registrationToken.setUin(user.getNin());
        String generatedString = RandomStringUtils.randomAlphanumeric(50);
        registrationToken.setToken(generatedString);

        return super.save(registrationToken);
    }
}
