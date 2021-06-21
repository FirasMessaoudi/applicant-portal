/*
 *  Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

/**
 * Enum for languages used in the system
 * 
 * @author Aymen DHAOUI
 * 
 * @since 1.0.0
 */
public enum ELanguage {

	ARABIC("ar"), ENGLISH("en");

	private String code;

	private ELanguage(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static ELanguage nullSafeValueOf(String language) {
		return defaultedValueOf(language);
	}

	public static ELanguage defaultedValueOf(String language) {
		for (ELanguage lang : ELanguage.values()) {
			if (lang.code.equalsIgnoreCase(language)) {
				return lang;
			}
		}
		return ENGLISH;
	}
}
