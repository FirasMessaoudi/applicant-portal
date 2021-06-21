/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import com.elm.shj.applicant.portal.orm.test.DtoTest;

/**
 * Testing class for {@link JpaUserPasswordHistory}
 * 
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class JpaUserPasswordHistoryTest extends DtoTest<JpaUserPasswordHistory> {

	@Override
	protected JpaUserPasswordHistory getInstance() {
		return new JpaUserPasswordHistory();
	}

}
