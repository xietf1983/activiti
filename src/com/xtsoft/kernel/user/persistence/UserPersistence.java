package com.xtsoft.kernel.user.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.user.model.User;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class UserPersistence extends BasePersistence<User> {
	public User create(long userId) {
		User model = new User();
		model.setNew(true);
		model.setUserId(userId);
		return model;
	}

	public User update(User model) throws SystemException {
		if (findByPrimaryKey(model.getUserId()) != null) {
			update(SqlMapUtil.USER_UPDATE, model);
		} else {
			insert(SqlMapUtil.USER_INSERT, model);
		}
		return model;

	}

	public User findByPrimaryKey(long userId) throws SystemException {
		User model = (User) selectOne(SqlMapUtil.USER_FETCHBYPRIMARYKEY, userId);
		if (model != null) {
			List<HashMap> listOrgs = getOrgIds(model.getUserId());
			if (listOrgs != null && listOrgs.size() > 0) {
				String orgIds ="";
				StringBuffer bufer = new StringBuffer();
				for(HashMap p :listOrgs){
					bufer.append(String.valueOf(p.get("ORGANIZATIONID")));
					bufer.append(",");
				}
				if(!bufer.toString().equals("")){
					orgIds=bufer.substring(0, bufer.toString().length()-1);
				}
				model.setOrgIds(orgIds);
			}
		}
		return model;
	}

	public List<Map> findUserList(Map para, int start, int limit) throws SystemException {
		return selectPageList(SqlMapUtil.USER_LIST_FETCHBYPARAMTER, para, start, limit);
	}

	public int findUserCount(Map para) throws SystemException {
		return ((Long) selectOne(SqlMapUtil.USER_COUNT_FETCHBYPARAMTER, para)).intValue();
	}

	/**
	 * 判断是否有此角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public boolean containsRoles(long userId, long roleId) throws SystemException {
		HashMap para = new HashMap();
		para.put("USERID", userId);
		para.put("ROLEID", roleId);
		long count = (Long) selectOne(SqlMapUtil.USERS_ROLES_FETCHBYPRIMARYKEY, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public void addUserRoles(long roleId, long userId) throws SystemException {
		if (!containsRoles(userId, roleId)) {
			HashMap para = new HashMap();
			para.put("USERID", userId);
			para.put("ROLEID", roleId);
			getSqlSession().insert(SqlMapUtil.USERS_ROLES_INSERT, para);
		}
	}

	public void removeUserRoles(long roleId, long userId) throws SystemException {
		HashMap para = new HashMap();
		if (userId > 0) {
			para.put("USERID", userId);
		}
		if (roleId > 0) {
			para.put("ROLEID", roleId);
		}
		getSqlSession().delete(SqlMapUtil.USERS_ROLES_DELETE, para);
	}

	/**
	 * 判断是否有此角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public boolean containsOrg(long userId, long orgId) throws SystemException {
		HashMap para = new HashMap();
		para.put("USERID", userId);
		para.put("ORGID", orgId);
		Long count = (Long) selectOne(SqlMapUtil.USERS_ORG_FETCHBYPRIMARYKEY, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public List<HashMap> getOrgIds(long userId) throws SystemException {
		HashMap para = new HashMap();
		para.put("USERID", userId);
		return selectList(SqlMapUtil.USERS_ORG_LIST, para);

	}

	public void addUserOrg(long userId, long orgId) throws SystemException {
		if (!containsOrg(userId, orgId)) {
			HashMap para = new HashMap();
			para.put("USERID", userId);
			para.put("ORGID", orgId);
			getSqlSession().insert(SqlMapUtil.USERS_ORG_INSERT, para);
		}
	}

	public void removeUserOrg(long userId, long orgId) throws SystemException {
		HashMap para = new HashMap();
		if (userId > 0) {
			para.put("USERID", userId);
		}
		if (orgId > 0) {
			para.put("ORGID", orgId);
		}
		getSqlSession().delete(SqlMapUtil.USERS_ORG_DELETE, para);
	}

	/**
	 * 判断是否有此角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public boolean containsUserGroup(long userId, long groupId) throws SystemException {
		HashMap para = new HashMap();
		para.put("USERID", userId);
		para.put("GROUPID", groupId);
		Integer count = (Integer) selectOne(SqlMapUtil.USERS_GROUP_FETCHBYPRIMARYKEY, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public void addUserGroup(long userId, long groupId) throws SystemException {
		if (!containsUserGroup(userId, groupId)) {
			HashMap para = new HashMap();
			para.put("USERID", userId);
			para.put("GROUPID", groupId);
			getSqlSession().insert(SqlMapUtil.USERS_GROUP_INSERT, para);
		}
	}

	public void removeUserGroup(long userId, long groupId) throws SystemException {
		HashMap para = new HashMap();
		if (userId > 0) {
			para.put("USERID", userId);
		}
		if (groupId > 0) {
			para.put("GROUPID", groupId);
		}
		getSqlSession().delete(SqlMapUtil.USERS_GROUP_DELETE, para);
	}

	public List<Map> findUserListByRoleId(HashMap para, int start, int limit) throws SystemException {
		return selectPageList(SqlMapUtil.USER_LIST_FETCHBYROLEID, para, start, limit);
	}

	public long findUserCountByRoleId(HashMap para) throws SystemException {
		return (Long) selectOne(SqlMapUtil.USER_COUNT_FETCHBYROLEID, para);
	}

	private static Log _log = LogFactory.getLog(UserPersistence.class);

}
