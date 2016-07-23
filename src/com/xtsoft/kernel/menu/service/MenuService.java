package com.xtsoft.kernel.menu.service;

import java.util.ArrayList;
import java.util.List;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.menu.model.Menu;
import com.xtsoft.kernel.menu.persistence.MenuPersistence;

public class MenuService {
	private MenuPersistence persistence;

	public MenuPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(MenuPersistence persistence) {
		this.persistence = persistence;
	}

	public List<Menu> getMenuByUserIdAndParent(long userId, long parent) throws SystemException {
		return getPersistence().getMenuByUserIdAndParent(userId, parent);

	}
	
	public boolean hasRight(String url,long userId) {
		if(getPersistence().isProtectedResource(url)){
			return getPersistence().hasRight(url, userId);
		}else{
			return true;
		}
	}
	
	public boolean isProtectedResource(String url) {
		return getPersistence().isProtectedResource(url);
	}

	public List<Menu> getMenuByParent(Menu menu) throws SystemException {
		build(menu);
		return menu.getChildren();
	}

	private void build(Menu menu) throws SystemException {
		List<Menu> children = getChildren(menu);
		if (children != null && children.size() > 0) {
			menu.setExpanded(true);
			for (Menu child : children) {
				build(child);
			}
		} else {
			menu.setLeaf(true);
		}
	}

	private List<Menu> getChildren(Menu menu) throws SystemException {
		List<Menu> children = getPersistence().getMenuByParent(menu);
		menu.setChildren(children);
		return children;
	}

}
