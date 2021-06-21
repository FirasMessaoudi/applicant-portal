/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import com.elm.shj.applicant.portal.orm.test.DtoTest;

/**
 * Testing class for {@link JpaRole}
 * 
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class JpaRoleTest extends DtoTest<JpaRole> {

	@Override
	protected JpaRole getInstance() {
		return new JpaRole();
	}

}
