/*
 *  Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.test;

import com.elm.shj.applicant.portal.orm.TestJpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.persistence.EntityManager;

/**
 * Parent class for all repository tests
 *
 * @author Aymen Dhaoui
 * @since 1.8.0
 */
@DataJpaTest
@ContextConfiguration(classes = TestJpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SqlGroup({
        @Sql(scripts = {"/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(statements = "TRUNCATE SCHEMA PUBLIC AND COMMIT NO CHECK", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public abstract class AbstractJpaTest {
    @Autowired
    protected EntityManager entityManager;
}
