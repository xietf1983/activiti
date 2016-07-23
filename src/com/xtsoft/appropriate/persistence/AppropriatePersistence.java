package com.xtsoft.appropriate.persistence;

import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.appropriate.model.Appropriate;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.reimburse.model.Reimburse;

public class AppropriatePersistence extends BasePersistence<Appropriate> {

	public List<Map> findAppropriateList(Map para, int start, int limit) throws SystemException {
		if (limit != 0) {
			return selectPageList(SqlMapUtil.APPROPRIATE_LIST_FETCHBYPARAMTER, para, start, limit);
		} else {
			return selectList(SqlMapUtil.APPROPRIATE_LIST_FETCHBYPARAMTER, para);
		}
	}

	public long findAppropriateCount(Map para) throws SystemException {
		return ((Long) selectOne(SqlMapUtil.APPROPRIATE_COUNT_FETCHBYPARAMTER, para)).intValue();
	}

	public Appropriate findByPrimaryKey(long id) throws SystemException {
		return (Appropriate) selectOne(SqlMapUtil.APPROPRIATE_FETCHBYPRIMARYKEY, id);
	}

	public Appropriate update(Appropriate model) throws SystemException {
		if (findByPrimaryKey(model.getAppropriateId()) != null) {
			update(SqlMapUtil.APPROPRIATE_UPDATE, model);
		} else {
			insert(SqlMapUtil.APPROPRIATE_INSERT, model);
		}
		return model;

	}

	public Appropriate removeAppropriate(long id) throws SystemException {
		Appropriate model = findByPrimaryKey(id);
		if (model != null) {
			model.setActive(0);
			getSqlSession().update(SqlMapUtil.APPROPRIATE_UPDATE, model);

		}
		return model;
	}
}
