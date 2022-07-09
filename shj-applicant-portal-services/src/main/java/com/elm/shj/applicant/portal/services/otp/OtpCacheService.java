package com.elm.shj.applicant.portal.services.otp;

import com.elm.shj.applicant.portal.orm.entity.JpaOtpCache;
import com.elm.shj.applicant.portal.orm.repository.OtpCacheRepository;
import com.elm.shj.applicant.portal.services.dto.OtpCacheDto;
import com.elm.shj.applicant.portal.services.generic.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OtpCacheService extends GenericService<JpaOtpCache, OtpCacheDto, Long> {

    public final OtpCacheRepository otpCacheRepository;

    public Optional<OtpCacheDto> findByPrincipleAndOtp(String principle, String otp) {
        log.info("Start findByPrincipleAndOtp for {} principle and {} otp value.", principle, otp);
        Optional<JpaOtpCache> otpCache = otpCacheRepository.findDistinctTopByPrincipleAndOtpOrderByCreationDate(principle, otp);
        if (otpCache.isPresent()) {
            return Optional.of(getMapper().fromEntity(otpCache.get(), mappingContext));
        }
        log.info("Finish findByPrincipleAndOtp for {} principle and {} otp value, no value found.", principle, otp);
        return Optional.empty();
    }

    @Transactional
    public void deletePrincipleOtps(String principle) {
        otpCacheRepository.deleteAllByPrinciple(principle);
    }
}
