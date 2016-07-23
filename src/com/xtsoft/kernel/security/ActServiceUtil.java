package com.xtsoft.kernel.security;

import org.apache.log4j.Logger;

import com.xtsoft.kernel.security.service.ActService;


public class ActServiceUtil {

	private static ActService _service;

	public static ActService getService() {
		return _service;
	}

	public void setService(ActService service) {
		_service = service;
	}

	private static Logger _log = Logger.getLogger(ActService.class);

}
