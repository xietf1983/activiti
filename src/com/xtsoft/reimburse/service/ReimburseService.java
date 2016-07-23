package com.xtsoft.reimburse.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.appropriate.model.Appropriate;
import com.xtsoft.category.model.Category;
import com.xtsoft.exception.SystemException;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.reimburse.model.ReimburseDetail;
import com.xtsoft.reimburse.persistence.ReimbursePersistence;
import com.xtsoft.workflow.WorkflowEngineUtil;

public class ReimburseService {
	private ReimbursePersistence persistence;

	public ReimbursePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(ReimbursePersistence persistence) {
		this.persistence = persistence;
	}

	public long findReimburseCount(Map para) throws SystemException {
		return getPersistence().findReimburseCount(para);
	}

	public void removeReimburse(long id) throws SystemException {
		Reimburse model = getReimburseByPrimaryKey(id);
		if (model.getStatus() == 1 || model.getStatus() == 0 || model.getStatus() == 3) {
			getPersistence().removeReimburse(model);
			WorkflowEngineUtil.getActivitiWorkflowEngine().cancelWorkflow(model.getProcessInstanceId());
		}

	}

	public List<Map> findReimburseList(Map para, int start, int limit) throws SystemException {
		return getPersistence().findReimburseList(para, start, limit);
	}

	public List<Map> findReimburseDetailList(long reimburseId) throws SystemException {
		return getPersistence().findReimburseDetailList(reimburseId);
	}

	public Reimburse getReimburseByPrimaryKey(long reimburseId) throws SystemException {
		return getPersistence().findByPrimaryKey(reimburseId);

	}

	public Reimburse create(long id) {
		Reimburse model = new Reimburse();
		model.setNew(true);
		model.setId(id);
		return model;
	}

	public ReimburseDetail createReimburseDetail(long id) {
		ReimburseDetail model = new ReimburseDetail();
		model.setNew(true);
		model.setDetailId(id);
		return model;
	}

	public Reimburse update(Reimburse model, List<ReimburseDetail> list, boolean done) throws SystemException {
		Reimburse r = getPersistence().update(model);
		if (done) {
			getPersistence().deleteReimburseAllDetail(model.getId());
			for (ReimburseDetail d : list) {
				d.setCompanyId(model.getCompanyId());
				d.setStatus(model.getStatus());
				d.setType(model.getType());
				d.setCreateDate(model.getCreateDate());
				getPersistence().addReimburseDetail(d);
			}
		}else{
			//¸üÐÂ×´Ì¬
			getPersistence().updateReimburseDetailStatus(model);
		}
		return r;

	}

	public String getNextSeqCode() throws SystemException {
		return getPersistence().getNextSeqCode();
	}
	
	public List<Map>  getReimburseStaticList(Map para) throws SystemException {
		return getPersistence().getReimburseStaticList(para);
	}

	private static Log _log = LogFactory.getLog(ReimburseService.class);
}
