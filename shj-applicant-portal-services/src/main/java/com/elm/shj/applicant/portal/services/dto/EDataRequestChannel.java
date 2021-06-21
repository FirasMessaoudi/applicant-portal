/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

/**
 * Enum for channels used in the system for data request
 * 
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public enum EDataRequestChannel {

	SYSTEM, WEB_SERVICE;

	public static EDataRequestChannel nullSafeValueOf(String channelStr) {
		for (EDataRequestChannel channel : EDataRequestChannel.values()) {
			if (channel.name().equalsIgnoreCase(channelStr)) { return channel; }
		}
		return SYSTEM;
	}

}
