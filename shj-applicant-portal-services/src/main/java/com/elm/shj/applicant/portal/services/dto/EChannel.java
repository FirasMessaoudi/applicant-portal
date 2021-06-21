/*
 * Copyright (c) 2017 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.dto;

/**
 * Enum for channels used in the system
 * 
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
public enum EChannel {

	TABLET, MOBILE, WEB, UNKOWN;

	public static EChannel nullSafeValueOf(String channelStr) {
		for (EChannel channel : EChannel.values()) {
			if (channel.name().equalsIgnoreCase(channelStr)) { return channel; }
		}
		return WEB;
	}

}
