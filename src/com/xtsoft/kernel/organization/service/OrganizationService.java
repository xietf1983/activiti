package com.xtsoft.kernel.organization.service;

import java.util.List;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.menu.model.Menu;
import com.xtsoft.kernel.organization.model.Organization;
import com.xtsoft.kernel.organization.persistence.OrganizationPersistence;

public class OrganizationService {

	private OrganizationPersistence persistence;

	public OrganizationPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(OrganizationPersistence persistence) {
		this.persistence = persistence;
	}

	public Organization getOrganizationByPrimaryKey(long organizationId) throws SystemException {
		return getPersistence().findByPrimaryKey(organizationId);

	}

	public Organization update(Organization model) throws SystemException {
		return getPersistence().update(model);

	}

	public List<Organization> getOrganizationByParentId(long parentId) throws SystemException {
		return getPersistence().getOrganizationByParentId(parentId);
	}

	public Organization create(long organizationId) {
		Organization model = new Organization();
		model.setNew(true);
		model.setOrganizationId(organizationId);
		return model;
	}
	
	public Organization getOrganizationByName(String text) throws SystemException {
		return getPersistence().getOrganizationByName(text);

	}

}
