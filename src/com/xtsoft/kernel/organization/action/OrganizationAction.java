package com.xtsoft.kernel.organization.action;

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

public class OrganizationAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(OrganizationAction.class);

	public String editOrganization() {
		Map map = new HashMap();
		try {
			String parentId = Struts2Utils.getParameter("PARENID");
			if (parentId == null || parentId.equals("")) {
				parentId = "0";
			}
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			String name = Struts2Utils.getParameter("NAME");
			String comments = Struts2Utils.getParameter("COMMENTS");
			Organization model = null;
			if (organizationId == null || organizationId.equals("")) {
				model = OrganizationServiceUtil.getService().create(CounterServiceUtil.increment(Organization.class.getName()));
				model.setComments(comments);
				model.setActive(1);
				model.setText(name);
				model.setCreateDate(new Date());
				model.setModifiedDate(new Date());
				if (parentId != null && parentId != "") {
					model.setParentOrganizationId(Long.parseLong(parentId));
					if (model.getParentOrganizationId() > 0) {
						Organization p = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(model.getParentOrganizationId());
						if (p != null) {
							model.setFullPath(p.getFullPath() + "|" + model.getOrganizationId()+ "|" );
						}
					} else {
						model.setFullPath("0|" + model.getOrganizationId()+ "|" );
					}
				} else {
					model.setParentOrganizationId(Long.parseLong(parentId));
					model.setFullPath("0|" + model.getOrganizationId()+ "|" );
				}
			} else {
				model = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(Long.parseLong(organizationId));
				model.setText(name);
				model.setComments(comments);
			}
			model.setSortId(model.getOrganizationId());
			OrganizationServiceUtil.getService().update(model);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("OrganizationAction.editOrganization" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String removeOrganization() {
		try {
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			Organization model = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(Long.parseLong(organizationId));
			if (model != null) {
				model.setActive(0);
				OrganizationServiceUtil.getService().update(model);
			}
		} catch (Exception e) {
			_log.error("OrganizationAction.removeOrganization" + "");
		}
		return null;
	}

	public String getOrganizationList() {
		List<Organization> list = null;
		Map map = new HashMap();
		try {
			String parentId = Struts2Utils.getParameter("PARENID");
			list = OrganizationServiceUtil.getService().getOrganizationByParentId(Long.parseLong(parentId));
			 map.put("total", list.size());
			 map.put("rows", list);

		} catch (Exception e) {
			// map.put("total", 0);
			// map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("OrganizationAction.getOrganizationList" + "");
		}
		Struts2Utils.renderDeepJson(list);
		return null;
	}
	
	public String getOrganizationGridList() {
		List<Organization> list = null;
		Map map = new HashMap();
		try {
			String parentId = Struts2Utils.getParameter("PARENID");
			list = OrganizationServiceUtil.getService().getOrganizationByParentId(Long.parseLong(parentId));
			 map.put("total", list.size());
			 map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("OrganizationAction.getOrganizationGridList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String getOrganizationByPrimaryKey() {
		Organization organization = null;
		try {
			String organizationId = Struts2Utils.getParameter("organizationId");
			organization = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(Long.parseLong(organizationId));
			Struts2Utils.renderJson(organization);
		} catch (Exception e) {
			_log.error("OrganizationAction.getStaticOrganization" + "");
		}
		return null;
	}
	
	public String updateSortOrganization() {
		Map map = new HashMap();
		try {
			String id1 = Struts2Utils.getParameter("organizationId");
			String id2 = Struts2Utils.getParameter("organizationId2");
			Organization mode1 = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(Long.parseLong(id1));
			Organization mode2 = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(Long.parseLong(id2));
			long sortid = mode1.getSortId();
			mode1.setSortId(mode2.getSortId());
			mode2.setSortId(sortid);
			OrganizationServiceUtil.getService().update(mode1);
			OrganizationServiceUtil.getService().update(mode2);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("CompanyAction.updateSortCompany" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String getOrganizationTree() {
		List retList = new ArrayList();
		try {
			String parentId = Struts2Utils.getParameter("parentId");
			if (parentId == null || parentId.equals("")) {
				parentId = "0";
			}
			List<Organization> list = OrganizationServiceUtil.getService().getOrganizationByParentId(Long.parseLong(parentId));
			for (Organization o : list) {
				HashMap hm = new HashMap();
				hm.put("id", o.getOrganizationId());
				hm.put("parentId", o.getParentOrganizationId());
				hm.put("text", o.getText());
				retList.add(hm);
			}

		} catch (Exception e) {

			e.printStackTrace();
			_log.error("OrganizationAction.getOrganizationTree" + "");
		}
		Struts2Utils.renderDeepJson(retList);
		return null;
	}
}
