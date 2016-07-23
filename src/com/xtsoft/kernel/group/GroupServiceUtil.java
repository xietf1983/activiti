package com.xtsoft.kernel.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.kernel.group.service.GroupService;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class GroupServiceUtil {
	private static GroupService _service;

	public static GroupService getService() {
		return _service;
	}

	public void setService(GroupService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(GroupServiceUtil.class);

}
