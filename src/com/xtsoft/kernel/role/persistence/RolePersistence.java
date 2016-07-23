package com.xtsoft.kernel.role.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.role.model.Role;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class RolePersistence extends BasePersistence<Role> {
	public Role create(long roleId) {
		Role model = new Role();
		model.setNew(true);
		model.setRoleId(roleId);
		return model;
	}

	public Role update(Role model) throws SystemException {
		if (findByPrimaryKey(model.getRoleId()) != null) {
			update(SqlMapUtil.ROLE_UPDATE, model);
		} else {
			insert(SqlMapUtil.ROLE_INSERT, model);
		}
		return model;

	}

	public Role findByPrimaryKey(long roleId) throws SystemException {
		Role model = (Role) selectOne(SqlMapUtil.ROLE_FETCHBYPRIMARYKEY, roleId);
		return model;
	}

	public void removeRole(long roleId) throws SystemException {
		getSqlSession().delete(SqlMapUtil.ROLE_DELETE, roleId);
	}

	public List<Map> findRoleList(Map role) {
		return selectList(SqlMapUtil.ROLE_LIST_FETCHBYMAP, role);

	}

	public List<String> getRoleMenuList(long roleId) throws SystemException {
		return selectList(SqlMapUtil.MENUUD_LIST_FETCHBYROLEID, roleId);
	}

	public void removeRoleMenu(long roleId, long menuId) throws SystemException {
		Map role = new HashMap();
		if (roleId > 0) {
			role.put("ROLEID", roleId);
		}
		if (menuId > 0) {
			role.put("MENUID", menuId);
		}
		getSqlSession().delete(SqlMapUtil.ROLE_MENU_DELETE, role);
	}

	public boolean containsRoleMenu(long roleId, long menuId) throws SystemException {
		HashMap para = new HashMap();
		para.put("ROLEID", roleId);
		para.put("MENUID", menuId);
		Long count = (Long) selectOne(SqlMapUtil.ROLE_MENU_CONTAINS, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public void addRoleMenu(long roleId, long menuId) throws SystemException {
		Map para = new HashMap();
		if (!containsRoleMenu(roleId, menuId)) {
			para.put("ROLEID", roleId);
			para.put("MENUID", menuId);
			getSqlSession().insert(SqlMapUtil.ROLE_MENU_INSERT, para);
		}
	}

	public List<Map> findUsersByRoleId(Map para, int start, int limit) {
		return selectPageList(SqlMapUtil.ROLE_LIST_FETCHBYMAP, para, start, limit);

	}

	private static Log _log = LogFactory.getLog(RolePersistence.class);

}
