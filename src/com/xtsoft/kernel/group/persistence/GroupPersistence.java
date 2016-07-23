package com.xtsoft.kernel.group.persistence;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.group.model.Group;
import com.xtsoft.kernel.role.model.Role;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class GroupPersistence extends BasePersistence<Group> {
	public Role create(long roleId) {
		Role model = new Role();
		model.setNew(true);
		model.setRoleId(roleId);
		return model;
	}

	public Group update(Group model) throws SystemException {
		if (findByPrimaryKey(model.getRoleId()) != null) {
			update(SqlMapUtil.GROUP_UPDATE, model);
		} else {
			insert(SqlMapUtil.GROUP_INSERT, model);
		}
		return model;

	}

	public Group findByPrimaryKey(long roleId) throws SystemException {
		Group model = (Group) selectOne(SqlMapUtil.GROUP_FETCHBYPRIMARYKEY, roleId);
		return model;
	}

	public List<Map> findRoleList(Map para) {
		return selectList(SqlMapUtil.GROUP_LIST_FETCHBYMAP, para);

	}

	private static Log _log = LogFactory.getLog(GroupPersistence.class);

}
