/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.user;

import com.elm.shj.applicant.portal.orm.entity.JpaUserPasswordHistory;
import com.elm.shj.applicant.portal.orm.repository.PasswordHistoryRepository;
import com.elm.shj.applicant.portal.services.dto.UserPasswordHistoryDto;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service handling password history operations
 *
 * @author Ahmad Flaifel
 * @since 1.3.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PasswordHistoryService extends GenericService<JpaUserPasswordHistory, UserPasswordHistoryDto, Long> {

    private final PasswordHistoryRepository passwordHistoryRepository;

    @Value("${dcc.validation.password.history.threshold}")
    private int passwordHistoryThreshold;


    /**
     * Find user passwords history ordered by creation date.
     *
     * @return the user passwords history
     */
    public List<UserPasswordHistoryDto> findByUserId(long userId) {
        return mapList(passwordHistoryRepository.findByUserIdOrderByCreationDateDesc(userId, PageRequest.of(0, passwordHistoryThreshold)));
    }

    /**
     * Find last user password history based on the creation date
     * @param userId the user id
     * @return the UserPasswordHistoryDto or empty
     */
    public Optional<UserPasswordHistoryDto> findLastByUserId(long userId) {
        JpaUserPasswordHistory passwordHistory =
                passwordHistoryRepository.findFirst1ByUserIdOrderByCreationDateDesc(userId);
        return (passwordHistory == null) ? Optional.empty() : Optional.of(getMapper().fromEntity(passwordHistory, mappingContext));
    }

    /**
     * Add a password history record for the user.
     *
     * @param userId
     * @param oldPasswordHash
     */
    public void addUserPasswordHistory(long userId, String oldPasswordHash) {
        UserPasswordHistoryDto userPasswordHistoryDto = new UserPasswordHistoryDto();
        userPasswordHistoryDto.setUserId(userId);
        userPasswordHistoryDto.setOldPasswordHash(oldPasswordHash);
        userPasswordHistoryDto.setCreationDate(new Date());
        this.save(userPasswordHistoryDto);
    }
}
