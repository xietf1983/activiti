package com.xtsoft.kernel.role;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.kernel.role.service.RoleService;


/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class RoleServiceUtil {
	private static RoleService _service;

	public static RoleService getService() {
		return _service;
	}

	public  void setService(RoleService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(RoleServiceUtil.class);

}
