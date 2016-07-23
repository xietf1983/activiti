package com.xtsoft.kernel.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.role.model.Role;
import com.xtsoft.kernel.role.persistence.RolePersistence;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.persistence.UserPersistence;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class RoleService {
	private RolePersistence persistence;

	private UserPersistence userPersistence;

	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public RolePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(RolePersistence persistence) {
		this.persistence = persistence;
	}

	public Role getRoleByPrimaryKey(long roleId) throws SystemException {
		return getPersistence().findByPrimaryKey(roleId);

	}

	public List<Map> findRoleList(Map role) {
		return getPersistence().findRoleList(role);
	}

	public Role update(Role model) throws SystemException {
		return getPersistence().update(model);

	}

	public Role create(long roleId) {

		return getPersistence().create(roleId);
	}

	public List getRoleMenuList(long roleId) throws SystemException {
		return getPersistence().getRoleMenuList(roleId);
	}

	public void removeRole(long roleId) throws SystemException {
		getPersistence().removeRole(roleId);
		getUserPersistence().removeUserRoles(0, roleId);
	}

	public void editRoleMenu(long roleId, String[] menuIds) throws SystemException {
		if (menuIds != null) {
			for (String s : menuIds) {
				if (s != null && !s.equals("")) {
					getPersistence().addRoleMenu(roleId, Long.parseLong(s));
				}
			}
		}
	}

	public void editRoleUserList(long roleId, String[] userIds) throws SystemException {
		if (userIds != null) {
			for (String s : userIds) {
				if (s != null && !s.equals("")) {
					getUserPersistence().addUserRoles(roleId, Long.parseLong(s));
				}
			}
		}
	}
	
	public void removeRoleUserList(long roleId, String[] userIds) throws SystemException {
		if (userIds != null) {
			for (String s : userIds) {
				if (s != null && !s.equals("")) {
					getUserPersistence().removeUserRoles(roleId, Long.parseLong(s));
				}
			}
		}
	}

	public List<Map> findUserListByRoleId(HashMap para, int start, int limit) throws SystemException {
		return getUserPersistence().findUserListByRoleId(para, start, limit);
	}

	public long findUserCountByRoleId(HashMap para) throws SystemException {
		return getUserPersistence().findUserCountByRoleId(para);
	}

	private static Log _log = LogFactory.getLog(RoleService.class);

}
