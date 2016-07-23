package com.xtsoft.kernel.security.service;

import java.util.HashMap;
import java.util.Map;

import com.xtsoft.kernel.menu.MenuServiceUtil;

public class ActService {
	public boolean hasRight(String url, long userId) {
		return MenuServiceUtil.getService().hasRight(url, userId);
	}

}
