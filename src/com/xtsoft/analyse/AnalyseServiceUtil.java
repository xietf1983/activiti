package com.xtsoft.analyse;

import com.xtsoft.analyse.service.AnalyseService;

public class AnalyseServiceUtil {
	private static AnalyseService service;

	public static AnalyseService getService() {
		return service;
	}

	public void setService(AnalyseService service) {
		AnalyseServiceUtil.service = service;
	}

}
