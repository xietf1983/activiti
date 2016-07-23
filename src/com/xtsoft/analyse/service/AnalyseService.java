package com.xtsoft.analyse.service;

import java.util.List;
import java.util.Map;

import com.xtsoft.analyse.persistence.AnalysePersistence;
import com.xtsoft.exception.SystemException;

public class AnalyseService {
	private AnalysePersistence persistence;

	public AnalysePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(AnalysePersistence persistence) {
		this.persistence = persistence;
	}

	public List<Map> findReimburseAnalyseList(Map para) throws SystemException {
		return getPersistence().findReimburseAnalyseList(para);
	}

	public List<Map> findAppropriateAnalyseList(Map para) throws SystemException {
		return getPersistence().findAppropriateAnalyseList(para);
	}
}
