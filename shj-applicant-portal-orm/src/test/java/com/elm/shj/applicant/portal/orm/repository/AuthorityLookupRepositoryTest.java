/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.repository;

import com.elm.shj.applicant.portal.orm.entity.JpaAuthorityLookup;
import com.elm.shj.applicant.portal.orm.test.AbstractJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing class for {@link AuthorityLookupRepository}
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
class AuthorityLookupRepositoryTest extends AbstractJpaTest {

    @Autowired
    private AuthorityLookupRepository authorityLookupRepository;

    @Test
    public void test_find_all_root_authorities() {
        List<JpaAuthorityLookup> rootAuthorities = authorityLookupRepository.findAllByParentIsNull();
        assertEquals(3, rootAuthorities.size());
    }

}
