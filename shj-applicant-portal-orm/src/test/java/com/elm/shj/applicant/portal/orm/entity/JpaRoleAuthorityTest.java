/*
 * Copyright (c) 2020 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.orm.entity;

import com.elm.shj.applicant.portal.orm.test.DtoTest;

/**
 * Testing class for {@link JpaRoleAuthority}
 * 
 * @author Aymen Dhaoui
 * @since 1.0.0
 */
public class JpaRoleAuthorityTest extends DtoTest<JpaRoleAuthority> {

	@Override
	protected JpaRoleAuthority getInstance() {
		return new JpaRoleAuthority();
	}

}
