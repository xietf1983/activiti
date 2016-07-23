package com.xtsoft.kernel.company.service;

import java.util.List;
import java.util.Map;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.company.model.Company;
import com.xtsoft.kernel.company.persistence.CompanyPersistence;

public class CompanyService {
	private CompanyPersistence persistence;

	public CompanyPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(CompanyPersistence persistence) {
		this.persistence = persistence;
	}

	public Company getCompanyByPrimaryKey(long companyId) throws SystemException {
		return getPersistence().findByPrimaryKey(companyId);

	}

	public List<Map> findCompanyList(Map role) {
		return getPersistence().findCompanyList(role);
	}

	public Company update(Company model) throws SystemException {
		return getPersistence().update(model);

	}

	public Company create(long companyId) {

		return getPersistence().create(companyId);
	}
	public Company findByName(String name) throws SystemException{
		return getPersistence().findByName(name);
	}

}
