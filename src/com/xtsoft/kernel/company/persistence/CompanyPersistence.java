package com.xtsoft.kernel.company.persistence;

import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.company.model.Company;
import com.xtsoft.kernel.role.model.Role;

public class CompanyPersistence extends BasePersistence<Company> {
	public Company create(long companyId) {
		Company model = new Company();
		model.setNew(true);
		model.setCompanyId(companyId);
		return model;
	}

	public Company update(Company model) throws SystemException {
		if (findByPrimaryKey(model.getCompanyId()) != null) {
			update(SqlMapUtil.COMPANY_UPDATE, model);
		} else {
			insert(SqlMapUtil.COMPANY_INSERT, model);
		}
		return model;

	}

	public Company findByPrimaryKey(long companyId) throws SystemException {
		Company model = (Company) selectOne(SqlMapUtil.COMPANY_FETCHBYPRIMARYKEY, companyId);
		return model;
	}

	public void removeCompany(long companyId) throws SystemException {
		getSqlSession().delete(SqlMapUtil.COMPANY_DELETE, companyId);
	}

	public List<Map> findCompanyList(Map map) {
		return selectList(SqlMapUtil.COMPANY_LIST_FETCHBYMAP, map);

	}

	public Company findByName(String name) throws SystemException {
		Company model = (Company) selectOne(SqlMapUtil.COMPANY_FETCHBYNAME, name);
		return model;
	}

}
