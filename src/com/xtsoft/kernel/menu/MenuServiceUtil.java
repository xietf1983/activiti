package com.xtsoft.kernel.menu;

import java.util.List;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.menu.model.Menu;
import com.xtsoft.kernel.menu.service.MenuService;



public class MenuServiceUtil {
	private static MenuService service;

	public static MenuService getService() {
		return service;
	}

	public void setService(MenuService service) {
		this.service = service;
	}

	public List<Menu> getMenuByUserIdAndParent(long userId, long parent) throws SystemException {
		return getService().getMenuByUserIdAndParent(userId, parent);
	}

}
