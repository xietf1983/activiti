package com.xtsoft.analyse.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.analyse.AnalyseServiceUtil;
import com.xtsoft.analyse.model.AnalyseModel;
import com.xtsoft.category.CategoryServiceUtil;
import com.xtsoft.category.model.Category;
import com.xtsoft.kernel.company.CompanyServiceUtil;
import com.xtsoft.kernel.company.model.Company;
import com.xtsoft.kernel.organization.OrganizationServiceUtil;
import com.xtsoft.kernel.organization.model.Organization;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.reimburse.ReimburseServiceUtil;

public class AnalyseAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(AnalyseAction.class);

	public String getReimburseAndAppropriateList() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String type = Struts2Utils.getParameter("TYPE");
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			String startTime = Struts2Utils.getParameter("STARTTIME");
			String endTime = Struts2Utils.getParameter("ENDTIME");
			String status = Struts2Utils.getParameter("STATUS");
			String companyId = Struts2Utils.getParameter("COMPANYID");
			UserShort currentUser = Struts2Utils.getCurrentUser();
			if (companyId != null && !companyId.equals("")) {
				hp.put("COMPANYID", companyId);
			}
			if (organizationId != null && !organizationId.equals("")) {
				hp.put("ORGANIZATIONIDS", organizationId);
			}
			if (organizationId != null && !organizationId.equals("")) {
				hp.put("ORGANIZATIONID", organizationId);
			}
			if (type != null && !type.equals("") && !type.equals("0")) {
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
			if (startTime != null && !startTime.equals("")) {
				hp.put("STARTDAY", startTime);
			}

			if (endTime != null && !endTime.equals("")) {
				hp.put("ENDDAY", endTime);
			}
			if (status != null && !status.equals("")) {
				hp.put("STATUS", status);
			}
			List<Map> reimburseList = AnalyseServiceUtil.getService().findReimburseAnalyseList(hp);
			List<Map> appropriateList = AnalyseServiceUtil.getService().findAppropriateAnalyseList(hp);
			Map<String, AnalyseModel> data = new HashMap();
			if (reimburseList != null && reimburseList.size() > 0) {
				for (Map p : reimburseList) {
					String key = p.get("ORGANIZATIONID") + "-" + p.get("COMPANYID");
					AnalyseModel model = new AnalyseModel();
					model.setCompanyId(Long.parseLong(String.valueOf(p.get("COMPANYID"))));
					model.setOrganizationId(Long.parseLong(String.valueOf(p.get("ORGANIZATIONID"))));
					Company c = CompanyServiceUtil.getService().getCompanyByPrimaryKey(model.getCompanyId());
					model.setCompanyName(c.getName());
					Organization org = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(model.getOrganizationId());
					model.setOrganizationName(org.getText());
					model.setReimburseamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					model.setBalance(-new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					data.put(key, model);
				}
			}
			if (appropriateList != null && appropriateList.size() > 0) {
				for (Map p : appropriateList) {
					String key = p.get("ORGANIZATIONID") + "-" + p.get("COMPANYID");
					if (data.get(key) != null) {
						AnalyseModel model = data.get(key);
						model.setAppropriateamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
						model.setBalance(new BigDecimal(model.getBalance() + Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
						data.put(key, model);
					} else {
						AnalyseModel model = new AnalyseModel();
						model.setCompanyId(Long.parseLong(String.valueOf(p.get("COMPANYID"))));
						model.setOrganizationId(Long.parseLong(String.valueOf(p.get("ORGANIZATIONID"))));
						Company c = CompanyServiceUtil.getService().getCompanyByPrimaryKey(model.getCompanyId());
						model.setCompanyName(c.getName());
						Organization org = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(model.getOrganizationId());
						model.setOrganizationName(org.getText());
						model.setAppropriateamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
						model.setBalance(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
						data.put(key, model);
					}
				}
			}
			Set<String> key = data.keySet();
			List<AnalyseModel> dataList = new ArrayList();
			AnalyseModel total = new AnalyseModel();
			total.setCompanyName("ºÏ¼Æ");
			for (Iterator it = key.iterator(); it.hasNext();) {
				String s = (String) it.next();
				AnalyseModel analyse = data.get(s);
				dataList.add(analyse);
				total.setAppropriateamount(new BigDecimal(total.getAppropriateamount()).add(new BigDecimal(analyse.getAppropriateamount())).floatValue());
				total.setBalance(new BigDecimal(total.getBalance()).add(new BigDecimal(analyse.getBalance())).floatValue());
				total.setReimburseamount(new BigDecimal(total.getReimburseamount()).add(new BigDecimal(analyse.getReimburseamount())).floatValue());
			}
			total.setAppropriateamount(new BigDecimal(total.getAppropriateamount()).setScale(2, RoundingMode.HALF_UP).floatValue());
			total.setBalance(new BigDecimal(total.getBalance()).setScale(2, RoundingMode.HALF_UP).floatValue());
			total.setReimburseamount(new BigDecimal(total.getReimburseamount()).setScale(2, RoundingMode.HALF_UP).floatValue());
			dataList.add(total);
			map.put("total", dataList.size());
			map.put("rows", dataList);
		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("AnalyseAction.getReimburseAndAppropriateList" + "");
		}

		Struts2Utils.renderDeepJson(map);
		return null;

	}

}
