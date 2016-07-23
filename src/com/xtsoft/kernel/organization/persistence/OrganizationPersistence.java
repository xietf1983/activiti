package com.xtsoft.kernel.organization.persistence;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.organization.model.Organization;

public class OrganizationPersistence extends BasePersistence<Organization> {
	public Organization create(long organizationId) {
		Organization model = new Organization();
		model.setNew(true);
		model.setOrganizationId(organizationId);
		return model;
	}

	public Organization getOrganizationByName(String text) throws SystemException {
		Organization model = (Organization) selectOne(SqlMapUtil.ORGANIZATION_FETCHBYNAME, text);
		return model;
	}
	
	public Organization update(Organization model) throws SystemException {
		if (findByPrimaryKey(model.getOrganizationId()) != null) {
			update(SqlMapUtil.ORGANIZATION_UPDATE, model);
		} else {
			insert(SqlMapUtil.ORGANIZATION_INSERT, model);
		}
		return model;

	}

	public void removeOrganization(long organizationId) throws SystemException {
		getSqlSession().delete(SqlMapUtil.ORGANIZATION_DELETE, organizationId);
	}

	public Organization findByPrimaryKey(long organizationId) throws SystemException {
		Organization model = (Organization) selectOne(SqlMapUtil.ORGANIZATION_FETCHBYPRIMARYKEY, organizationId);
		return model;
	}

	public List<Organization> getOrganizationByParentId(long parentId) {
		return selectList(SqlMapUtil.ORGANIZATION_LIST_FETCHBYPARENTID, parentId);

	}

	private static Log _log = LogFactory.getLog(OrganizationPersistence.class);

}
