package com.xtsoft.kernel.user;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xtsoft.kernel.user.service.UserService;


/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class UserServiceUtil {
	private static UserService _service;

	public static UserService getService() {
		return _service;
	}

	public  void setService(UserService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(UserServiceUtil.class);

}
