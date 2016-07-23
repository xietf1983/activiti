package com.xtsoft.reimburse.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.Constants;
import com.xtsoft.analyse.model.AnalyseModel;
import com.xtsoft.category.CategoryServiceUtil;
import com.xtsoft.category.model.Category;
import com.xtsoft.kernel.company.CompanyServiceUtil;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.reimburse.ReimburseServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.reimburse.model.ReimburseDetail;
import com.xtsoft.util.DateUtil;
import com.xtsoft.workflow.WorkflowEngineUtil;

public class ReimburseAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(ReimburseAction.class);

	public String getReimburseList() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String code = Struts2Utils.getParameter("PROJECTCODE");
			String startTime = Struts2Utils.getParameter("STARTTIME");
			String endTime = Struts2Utils.getParameter("ENDTIME");
			String status = Struts2Utils.getParameter("STATUS");
			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			// String operation = Struts2Utils.getParameter("OPERATION");
			UserShort currentUser = Struts2Utils.getCurrentUser();
			if (currentUser != null) {
				hp.put("CREATEOR", currentUser.getUserId());
			} else {
				hp.put("CREATEOR", "1");
			}
			if (code != null && !code.equals("")) {
				hp.put("SEQCODE", "%" + code + "%");
			}

			if (startTime != null && !startTime.equals("")) {
				hp.put("STARTDAY", startTime);
			}

			if (endTime != null && !endTime.equals("")) {
				hp.put("ENDDAY", endTime);
			}
			if (status != null && !status.equals("")) {
				hp.put("STATUS", status);
			}
			map.put("total", ReimburseServiceUtil.getService().findReimburseCount(hp));
			map.put("rows", ReimburseServiceUtil.getService().findReimburseList(hp, Integer.parseInt(start), Integer.parseInt(limit)));

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("ReimburseAction.ReimburseAction" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String getReimburseDetailList() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String reimburseId = Struts2Utils.getParameter("REIMBURSEID");
			if (reimburseId != null && !reimburseId.equals("")) {
			} else {
				reimburseId = "0";
			}
			List<Map> list = ReimburseServiceUtil.getService().findReimburseDetailList(Long.parseLong(reimburseId));
			map.put("total", list.size());
			map.put("rows", list);
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("ReimburseAction.getReimburseDetalList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String removeReimburse() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String reimburseId = Struts2Utils.getParameter("REIMBURSEID");
			ReimburseServiceUtil.getService().removeReimburse(Long.parseLong(reimburseId));
			map.put("msg", "1");
		} catch (Exception e) {
			map.put("msg", "1");
			_log.error("ReimburseAction.removetReimburse" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String startWorkFlowReimburse() {
		Map map = new HashMap();
		try {
			String type = Struts2Utils.getParameter("TYPE");
			String companyId = Struts2Utils.getParameter("COMPANYID");
			String nextUser = Struts2Utils.getParameter("NEXTUSER");
			String reimburseId = Struts2Utils.getParameter("REIMBURSEID");
			String jsonList = Struts2Utils.getParameter("JSONLIST");
			UserShort usershort = Struts2Utils.getCurrentUser();
			Reimburse model = null;
			List<ReimburseDetail> list = null;
			if (reimburseId == null || reimburseId.equals("")) {
				model = ReimburseServiceUtil.getService().create(CounterServiceUtil.increment(Reimburse.class.getName()));
				model.setActive(1);
				list = ReimburseServiceUtil.buildReimburseDetailListFromJson(jsonList, model.getId(), usershort.getUserId());
				model.setCreateor(Struts2Utils.getCurrentUser().getUserId());
				model.setCreateDate(new Date());
				model.setStatus(Constants.REIMBURSE_STATUS_1);
				model.setUserId(Struts2Utils.getCurrentUser().getUserId());
				model.setProcessInstanceId(null);
				model.setCompanyId(Long.parseLong(companyId));
				model.setSeqCode(ReimburseServiceUtil.getService().getNextSeqCode());
				model.setSeqindex(Long.parseLong(model.getSeqCode().substring(7)));
				if (nextUser != null && !nextUser.equals("")) {
					model.setNextUser(Long.parseLong(nextUser));
				}
				model.setType(Long.parseLong(type));
				float amount = 0.0f;
				for (ReimburseDetail r : list) {
					amount = amount + r.getAmount();
				}
				model.setAmount(amount);
			} else {
				model = ReimburseServiceUtil.getService().getReimburseByPrimaryKey(Long.parseLong(reimburseId));
				ReimburseServiceUtil.buildReimburseDetailListFromJson(jsonList, model.getId(), usershort.getUserId());
				model.setCompanyId(Long.parseLong(companyId));
				model.setType(Long.parseLong(type));
				if (nextUser != null && !nextUser.equals("")) {
					model.setNextUser(Long.parseLong(nextUser));
				}
				float amount = 0.0f;
				for (ReimburseDetail r : list) {
					amount = amount + r.getAmount();
				}
				model.setAmount(amount);
			}
			ReimburseServiceUtil.getService().update(model, list, true);
			Category c = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(type));
			if (c != null && c.getDefkey() != null) {
				Map<String, Object> variables = new HashMap();
				variables.put("NEXTUSER", nextUser);
				String taskName = Struts2Utils.getCurrentUser().getUserName() + "报销登记(" + DateUtil.toString(model.getCreateDate()) + ")" + "-";
				ProcessInstance p = WorkflowEngineUtil.getActivitiWorkflowEngine().startWorkflowFirstTaskEnd(c.getDefkey(), String.valueOf(model.getId()), variables, String.valueOf(Struts2Utils.getCurrentUser().getUserId()), taskName);
				if (p != null) {
					model = ReimburseServiceUtil.getService().getReimburseByPrimaryKey(model.getId());
					model.setProcessInstanceId(p.getProcessInstanceId());
					model.setStatus(Constants.REIMBURSE_STATUS_2);
					ReimburseServiceUtil.getService().update(model, null, false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("ReimburseAction.editReimburse" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String loadReimburse() {
		Reimburse model = null;
		try {
			String reimburseId = Struts2Utils.getParameter("REIMBURSEID");
			if (reimburseId == null || !reimburseId.equals("")) {
				model = ReimburseServiceUtil.getService().getReimburseByPrimaryKey(Long.parseLong(reimburseId));
				if (model != null) {
					model.setCreateDateStr(DateUtil.toString(model.getCreateDate()));
					model.setCreateorName(UserServiceUtil.getService().findByPrimaryKey(model.getCreateor()).getUserName());
					// model.setCompanyName(CompanyServiceUtil.getService().getCompanyByPrimaryKey(model.getCompanyId()).getName());
					// model.setTypeName(CategoryServiceUtil.getService().getCategoryByPrimaryKey(model.getType()).getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("ReimburseAction.loadReimburse" + "");
		}
		Struts2Utils.renderDeepJson(model);
		return null;
	}

	public String editReimburse() {
		Map map = new HashMap();
		try {
			String type = Struts2Utils.getParameter("TYPE");
			String companyId = Struts2Utils.getParameter("COMPANYID");
			String nextUser = Struts2Utils.getParameter("NEXTUSER");
			String reimburseId = Struts2Utils.getParameter("REIMBURSEID");
			String jsonList = Struts2Utils.getParameter("JSONLIST");
			Reimburse model = null;
			List<ReimburseDetail> list = null;
			UserShort usershort = Struts2Utils.getCurrentUser();
			if (reimburseId == null || reimburseId.equals("")) {
				model = ReimburseServiceUtil.getService().create(CounterServiceUtil.increment(Reimburse.class.getName()));
				list = ReimburseServiceUtil.buildReimburseDetailListFromJson(jsonList, model.getId(), usershort.getUserId());
				model.setActive(1);
				model.setCreateor(Struts2Utils.getCurrentUser().getUserId());
				model.setCreateDate(new Date());
				model.setStatus(Constants.REIMBURSE_STATUS_1);
				model.setUserId(Struts2Utils.getCurrentUser().getUserId());
				model.setProcessInstanceId(null);
				model.setCompanyId(Long.parseLong(companyId));
				model.setSeqCode(ReimburseServiceUtil.getService().getNextSeqCode());
				if (nextUser != null && !nextUser.equals("")) {
					model.setNextUser(Long.parseLong(nextUser));
				}
				model.setType(Long.parseLong(type));
				//float amount = 0.0f;
				BigDecimal amount = new BigDecimal( 0.0f);
				for (ReimburseDetail r : list) {
					//amountnew BigDecimal(total.getAppropriateamount()
					amount = amount.add(new BigDecimal(r.getAmount()));
				}
				model.setAmount(amount.floatValue());
			} else {
				model = ReimburseServiceUtil.getService().getReimburseByPrimaryKey(Long.parseLong(reimburseId));
				list = ReimburseServiceUtil.buildReimburseDetailListFromJson(jsonList, model.getId(), usershort.getUserId());
				model.setCompanyId(Long.parseLong(companyId));
				model.setType(Long.parseLong(type));
				if (nextUser != null && !nextUser.equals("")) {
					model.setNextUser(Long.parseLong(nextUser));
				}
				BigDecimal amount = new BigDecimal( 0.0f);
				for (ReimburseDetail r : list) {
					//amountnew BigDecimal(total.getAppropriateamount()
					amount = amount.add(new BigDecimal(r.getAmount()));
				}
				model.setAmount(amount.floatValue());
			}
			ReimburseServiceUtil.getService().update(model, list, true);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("ReimburseAction.editReimburse" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String getReimburseStaticList() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			String companyId = Struts2Utils.getParameter("COMPANYID");
			String startTime = Struts2Utils.getParameter("STARTTIME");
			String endTime = Struts2Utils.getParameter("ENDTIME");
			String status = Struts2Utils.getParameter("STATUS");
			String userIds = Struts2Utils.getParameter("JSONLIST");
			String type = Struts2Utils.getParameter("TYPE");
			
			UserShort currentUser = Struts2Utils.getCurrentUser();
			if (startTime != null && !startTime.equals("")) {
				hp.put("STARTDAY", startTime);
			}

			if (endTime != null && !endTime.equals("")) {
				hp.put("ENDDAY", endTime);
			}
			if (status != null && !status.equals("")) {
				hp.put("STATUS", status);
			}
			if (companyId != null && !companyId.equals("")) {
				hp.put("COMPANYID", companyId);
			}
			if (organizationId != null && !organizationId.equals("")) {
				hp.put("ORGANIZATIONID", organizationId);
			}
			if (userIds != null && !userIds.equals("")) {
				hp.put("APPROPRIATEUSERID", userIds);
			}
			if (type != null && !type.equals("")&& !type.equals("0") ) {
				Category c = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(type));
				if (c != null) {
					List<Category> categoryList = CategoryServiceUtil.getService().findByFullPath(c.getFullPath());
					StringBuffer categoryBuffer = new StringBuffer();
					for (int j = 0; j < categoryList.size(); j++) {
						if (j == 0) {
							categoryBuffer.append(categoryList.get(j).getCategoryId());
						} else {
							categoryBuffer.append(",");
							categoryBuffer.append(categoryList.get(j).getCategoryId());
						}
					}
					if (!categoryBuffer.toString().equals("")) {
						hp.put("TYPES", categoryBuffer.toString());
					}
				}
			}
			StringBuffer orgbuffer = new StringBuffer();
			List<HashMap> listMap = UserServiceUtil.getService().getOrgIds(currentUser.getUserId());
			if (listMap != null && listMap.size() > 0) {
				for (int j = 0; j < listMap.size(); j++) {
					if (j == 0) {
						orgbuffer.append(listMap.get(j).get("ORGANIZATIONID"));
					} else {
						orgbuffer.append(",");
						orgbuffer.append(listMap.get(j).get("ORGANIZATIONID"));
					}
				}
			} else {
				orgbuffer.append("0");
			}
			hp.put("ORGIDS", orgbuffer.toString());
			List<Map> data = ReimburseServiceUtil.getService().getReimburseStaticList(hp);
			if (data == null) {
				data = new ArrayList();
			}
			Map totalMap = new HashMap();
			map.put("total", data.size());
			map.put("rows", data);
			totalMap.put("CREATEDATE_SHOWVALUE", "");
			totalMap.put("CREATEOR_SHOWVALUE", "");
			totalMap.put("COMPANY_SHOWVALUE", "");
			totalMap.put("CATEGORY_SHOWVALUE", "");
			totalMap.put("USERNAME", "合计");
			BigDecimal totalMout = new BigDecimal(0);
			for (Map p : data) {
				totalMout = totalMout.add(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))));
			}
			totalMout = totalMout.setScale(2, RoundingMode.HALF_UP);
			totalMap.put("AMOUNT", totalMout);
			data.add(totalMap);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("ReimburseAction.getReimburseStaticList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

}
