package com.xtsoft.category;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.category.service.CategoryService;

public class CategoryServiceUtil {
	private static CategoryService _service;

	public static CategoryService getService() {
		return _service;
	}

	public void setService(CategoryService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(CategoryServiceUtil.class);
}
