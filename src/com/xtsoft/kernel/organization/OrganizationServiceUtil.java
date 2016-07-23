package com.xtsoft.kernel.organization;

import org.apache.log4j.Logger;

import com.xtsoft.kernel.organization.service.OrganizationService;

public class OrganizationServiceUtil {
	private static OrganizationService _service;

	public static OrganizationService getService() {
		return _service;
	}

	public void setService(OrganizationService service) {
		_service = service;
	}

	private static Logger _log = Logger.getLogger(OrganizationServiceUtil.class);
}
