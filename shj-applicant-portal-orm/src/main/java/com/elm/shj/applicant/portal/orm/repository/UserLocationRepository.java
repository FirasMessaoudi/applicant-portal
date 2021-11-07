package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaUserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for user location table.
 *
 * @author Jaafer Jarray
 * @since 1.3.0
 */
public interface UserLocationRepository extends JpaRepository<JpaUserLocation, Long> {
}
