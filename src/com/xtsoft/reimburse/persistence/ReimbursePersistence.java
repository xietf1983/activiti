package com.xtsoft.reimburse.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.reimburse.model.ReimburseDetail;
import com.xtsoft.util.DateUtil;

public class ReimbursePersistence extends BasePersistence<Reimburse> {
	public List<Map> findReimburseList(Map para, int start, int limit) throws SystemException {
		return selectPageList(SqlMapUtil.REIMBURSE_LIST_FETCHBYPARAMTER, para, start, limit);
	}

	public long findReimburseCount(Map para) throws SystemException {
		return ((Long) selectOne(SqlMapUtil.REIMBURSE_COUNT_FETCHBYPARAMTER, para)).intValue();
	}

	public Reimburse findByPrimaryKey(long reimburseId) throws SystemException {
		Reimburse model = (Reimburse) selectOne(SqlMapUtil.REIMBURSE_FETCHBYPRIMARYKEY, reimburseId);
		return model;
	}

	public ReimburseDetail findReimburseDetailByPrimaryKey(long detailId) throws SystemException {
		ReimburseDetail model = (ReimburseDetail) selectOne(SqlMapUtil.REIMBURSEDETAIL_FETCHBYPRIMARYKEY, detailId);
		return model;
	}

	public List<Map> findReimburseDetailList(long reimburseId) throws SystemException {
		return selectList(SqlMapUtil.REIMBURSEDETAIL_LIST_FETCHBYKEY, reimburseId);
	}

	public List<Map> getReimburseStaticList(Map para) throws SystemException {
		return selectList(SqlMapUtil.REIMBURSE_STATIC_FETCHBYPARAMTER, para);
	}

	public Reimburse create(long appropriateId) {
		Reimburse model = new Reimburse();
		model.setNew(true);
		model.setId(appropriateId);
		return model;
	}

	public ReimburseDetail createReimburseDetail(long detailId) {
		ReimburseDetail model = new ReimburseDetail();
		model.setNew(true);
		model.setDetailId(detailId);
		return model;
	}

	public Reimburse update(Reimburse model) throws SystemException {
		if (findByPrimaryKey(model.getId()) != null) {
			update(SqlMapUtil.REIMBURSE_UPDATE, model);
		} else {
			insert(SqlMapUtil.REIMBURSE_INSERT, model);
		}
		return model;

	}

	public void removeReimburseAllDetail(long reimburseId) throws SystemException {
		ReimburseDetail model = new ReimburseDetail();
		model.setReimburseId(reimburseId);
		model.setActive(0);
		getSqlSession().update(SqlMapUtil.REIMBURSE_UPDATE, model);
	}

	public ReimburseDetail addReimburseDetail(ReimburseDetail model) throws SystemException {
		getSqlSession().insert(SqlMapUtil.REIMBURSEDETAIL_INSERT, model);
		return model;

	}

	public void removeReimburse(Reimburse model) throws SystemException {
		model = findByPrimaryKey(model.getId());
		if (model != null) {
			model.setActive(0);
			getSqlSession().update(SqlMapUtil.REIMBURSE_UPDATE, model);
			if (model != null) {
				// removeReimburseAllDetail(model.getId());
			}
		}
	}

	public void deleteReimburseDetail(ReimburseDetail model) throws SystemException {
		Map para = new HashMap();
		if (model != null) {
			para.put("DETAILID", model.getDetailId());
			getSqlSession().delete(SqlMapUtil.REIMBURSEDETAIL_DELETE, para);
		}
	}
	
	
	public void updateReimburseDetailStatus(Reimburse model) throws SystemException {
		Map para = new HashMap();
		if (model != null) {
			para.put("STATUS", model.getStatus());
			getSqlSession().update(SqlMapUtil.REIMBURSEDETAIL_STATUS_UPDATE, model);
		}
	}

	public void deleteReimburseAllDetail(long reimburseId) throws SystemException {
		Map para = new HashMap();
		para.put("REIMBURSEID", reimburseId);
		getSqlSession().update(SqlMapUtil.REIMBURSEDETAIL_DELETE, para);

	}

	public String getNextSeqCode() throws SystemException {
		Date currentDate = new Date();
		String dateStart = DateUtil.toString(currentDate);
		dateStart = dateStart.substring(0, 7) + "-01";
		Map para = new HashMap();
		para.put("STARTDATE", dateStart.substring(0, 7) + "-01");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		String dateEnd = DateUtil.toString(calendar.getTime());
		para.put("ENDDATE", dateEnd.substring(0, 10));
		long value = ((Long) selectOne(SqlMapUtil.REIMBURSE_SEQCODE, para)).intValue() + 1;
		String buffervalue = String.valueOf(value);
		StringBuffer buffer = new StringBuffer();
		if (buffervalue.length() < 4) {
			for (int j = 0; j < 4 - buffervalue.length(); j++) {
				buffer.append("0");
			}
		}
		buffer.append(value);
		return dateStart.substring(0, 7).replaceAll("-", "") + buffer.toString();

	}
}
