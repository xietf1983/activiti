package com.xtsoft.analyse.persistence;

import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;

public class AnalysePersistence extends BasePersistence {
	public List<Map> findAppropriateAnalyseList(Map para) throws SystemException {
		return selectList(SqlMapUtil.APPROPRIATE_ANALYSE_FETCHBYPARAMTER, para);
	}
	
	public List<Map> findReimburseAnalyseList(Map para) throws SystemException {
		return selectList(SqlMapUtil.REIMBURSE_ANALYSE_FETCHBYPARAMTER, para);
	}

}
