package com.xtsoft.kernel.company.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.kernel.company.CompanyServiceUtil;
import com.xtsoft.kernel.company.model.Company;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.organization.OrganizationServiceUtil;
import com.xtsoft.kernel.organization.model.Organization;

public class CompanyAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(CompanyAction.class);

	public String getCompanyList() {
		Map map = new HashMap();
		try {

			List<Map> list = CompanyServiceUtil.getService().findCompanyList(null);
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("CompanyAction.getCompanyList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String updateSortCompany() {
		Map map = new HashMap();
		try {
			String id1 = Struts2Utils.getParameter("COMPANYID");
			String id2 = Struts2Utils.getParameter("COMPANYID2");
			String description = Struts2Utils.getParameter("DESCRIPTION");

			Company mode1 = CompanyServiceUtil.getService().getCompanyByPrimaryKey(Long.parseLong(id1));
			Company mode2 = CompanyServiceUtil.getService().getCompanyByPrimaryKey(Long.parseLong(id2));
			long sortid = mode1.getSortId();
			mode1.setSortId(mode2.getSortId());
			mode2.setSortId(sortid);
			CompanyServiceUtil.getService().update(mode1);
			CompanyServiceUtil.getService().update(mode2);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("CompanyAction.updateSortCompany" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String editCompany() {
		Map map = new HashMap();
		try {
			String name = Struts2Utils.getParameter("NAME");
			String id = Struts2Utils.getParameter("ID");
			String description = Struts2Utils.getParameter("DESCRIPTION");
			Company model = null;
			if (id == null || id.equals("")) {
				model = CompanyServiceUtil.getService().create(CounterServiceUtil.increment(Company.class.getName()));
				model.setName(name);
				model.setCreateDate(new Date());
				model.setSortId(model.getCompanyId());
				model.setActive(1);
				model.setDescription(description);
			} else {
				model = CompanyServiceUtil.getService().getCompanyByPrimaryKey(Long.parseLong(id));
				model.setName(name);
				model.setActive(1);
				model.setDescription(description);
			}
			CompanyServiceUtil.getService().update(model);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("CompanyAction.editCompany" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	/**
	 * É¾³ýÖ÷Ìå
	 * 
	 * @return
	 */
	public String deleteCompany() {
		Map map = new HashMap();
		try {
			String id = Struts2Utils.getParameter("ID");
			Company model = CompanyServiceUtil.getService().getCompanyByPrimaryKey(Long.parseLong(id));
			if (model != null) {
				model.setActive(0);
				CompanyServiceUtil.getService().update(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("CompanyAction.deleteCompany" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}
}
