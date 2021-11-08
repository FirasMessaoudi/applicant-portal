package com.elm.shj.applicant.portal.services.user;

import com.elm.shj.applicant.portal.orm.entity.JpaUserLocation;
import com.elm.shj.applicant.portal.services.dto.UserLocationDto;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserLocationService extends GenericService<JpaUserLocation, UserLocationDto, Long> {

    @Transactional
    public void storeUserLocation(List<UserLocationDto> locations) {
        this.saveAll(locations);
    }
}
