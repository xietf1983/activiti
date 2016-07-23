package com.xtsoft.kernel.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.user.model.User;
import com.xtsoft.kernel.user.persistence.UserPersistence;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class UserService {
	private UserPersistence persistence;

	public UserPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(UserPersistence persistence) {
		this.persistence = persistence;
	}

	public User create(long userId) throws SystemException {
		return getPersistence().create(userId);

	}

	public User update(User model, String[] orgIds) throws SystemException {
		getPersistence().update(model);
		if (orgIds != null && orgIds.length > 0) {
			getPersistence().removeUserOrg(model.getUserId(), 0);
			for (String orgId : orgIds) {
				if (!orgId.equals("")) {
					getPersistence().addUserOrg(model.getUserId(), Long.parseLong(orgId));
				}
			}
		}
		return model;
	}

	public User findByPrimaryKey(long userId) throws SystemException {
		return getPersistence().findByPrimaryKey(userId);
	}

	public List<Map> findUserList(Map para, int start, int limit) throws SystemException {
		return getPersistence().findUserList(para, start, limit);
	}

	public int findUserCount(Map para) throws SystemException {
		return getPersistence().findUserCount(para);
	}

	public void addUserRoles(long userId, long roleId) throws SystemException {
		getPersistence().addUserRoles(userId, roleId);
	}

	public void removeUserRoles(long userId, long roleId) throws SystemException {
		getPersistence().removeUserRoles(userId, roleId);
	}
	
	public List<HashMap> getOrgIds(long userId) throws SystemException{
		return getPersistence().getOrgIds(userId);
	}

	private static Log _log = LogFactory.getLog(UserService.class);

}
