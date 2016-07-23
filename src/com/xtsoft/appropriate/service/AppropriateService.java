package com.xtsoft.appropriate.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.appropriate.model.Appropriate;
import com.xtsoft.appropriate.persistence.AppropriatePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.reimburse.model.Reimburse;

public class AppropriateService {

	private AppropriatePersistence persistence;

	public AppropriatePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(AppropriatePersistence persistence) {
		this.persistence = persistence;
	}

	private static Log _log = LogFactory.getLog(AppropriateService.class);

	public long findAppropriateCount(Map para) throws SystemException {
		return getPersistence().findAppropriateCount(para);
	}

	public List<Map> findAppropriateList(Map para, int start, int limit) throws SystemException {
		return getPersistence().findAppropriateList(para, start, limit);
	}

	public Appropriate findByPrimaryKey(long id) throws SystemException {
		return getPersistence().findByPrimaryKey(id);
	}

	public Appropriate update(Appropriate model) throws SystemException {
		return getPersistence().update(model);
	}

	public Appropriate removeAppropriate(long id) throws SystemException {
		return getPersistence().removeAppropriate(id);
	}

	public Appropriate create(long id) {
		Appropriate model = new Appropriate();
		model.setNew(true);
		model.setAppropriateId(id);
		return model;
	}

	public void addAppropriateList(List<Appropriate> list) throws SystemException {
		if (list != null && list.size() > 0) {
			for (Appropriate model : list) {
				getPersistence().update(model);
			}
		}
	}
}
