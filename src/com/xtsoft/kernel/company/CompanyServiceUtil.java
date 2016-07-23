package com.xtsoft.kernel.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.kernel.company.service.CompanyService;


public class CompanyServiceUtil {
	private static CompanyService _service;

	public static CompanyService getService() {
		return _service;
	}

	public  void setService(CompanyService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(CompanyServiceUtil.class);


}
