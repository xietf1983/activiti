package com.xtsoft.category.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.category.CategoryServiceUtil;
import com.xtsoft.category.model.Category;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.organization.OrganizationServiceUtil;
import com.xtsoft.kernel.organization.model.Organization;

public class CategoryAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(CategoryAction.class);

	public String editCategory() {
		Map map = new HashMap();
		try {
			String parentId = Struts2Utils.getParameter("PARENTID");
			String id = Struts2Utils.getParameter("CATEGORYID");
			String name = Struts2Utils.getParameter("NAME");
			String remark = Struts2Utils.getParameter("REMARK");
			String description = Struts2Utils.getParameter("DESCRIPTION");
			String isLeaf = Struts2Utils.getParameter("ISLEAF");
			if (isLeaf == null || isLeaf.equals("")) {
				isLeaf = "1";
			}
			String defkey = Struts2Utils.getParameter("DEFKEY");
			String typeKey = Struts2Utils.getParameter("TYPEKEY");
			Category model = null;
			if (id == null || id.equals("")) {
				model = CategoryServiceUtil.getService().create(CounterServiceUtil.increment(Category.class.getName()));
				model.setName(name);
				model.setDefkey(defkey);
				model.setIsleaf(Integer.parseInt(isLeaf));
				model.setDefkey(defkey);
				model.setDescription(description);
				model.setParentId(Long.parseLong(parentId));
				model.setTypeKey(typeKey);
				model.setSortId(model.getCategoryId());
				model.setRemark(remark);
				model.setActive(1);
			} else {
				model = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(id));
				model.setName(name);
				model.setDefkey(defkey);
				model.setIsleaf(Integer.parseInt(isLeaf));
				model.setDefkey(defkey);
				model.setDescription(description);
				model.setParentId(Long.parseLong(parentId));
				model.setTypeKey(typeKey);
				model.setRemark(remark);
			}
			List<Category> list = CategoryServiceUtil.getService().getCategoryByParentId(model.getCategoryId(), null);
			if (list == null || list.size()==0) {
				model.setIsleaf(1);
			}else{
				model.setIsleaf(0);
			}
			CategoryServiceUtil.getService().update(model);
			if (Long.parseLong(parentId) > 0) {
				model = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(parentId));
				model.setIsleaf(0);
				CategoryServiceUtil.getService().update(model);
			}
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("CategoryAction.editCategory" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String removeCategory() {
		Map map = new HashMap();
		String ret = "0";
		try {
			String categoryId = Struts2Utils.getParameter("CATEGORYID");
			List<Category> list = CategoryServiceUtil.getService().getCategoryByParentId(Long.parseLong(categoryId), null);
			if (list == null || list.size()==0) {
				Category category = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(categoryId));
				if (category != null) {
					category.setActive(0);
					CategoryServiceUtil.getService().update(category);
					ret = "1";
					
				}
			} else {

			}

		} catch (Exception e) {
			_log.error("CategoryAction.removeCategory" + "");
		}
		map.put("RESULT", ret);
		Struts2Utils.renderDeepJson(ret);
		return null;
	}

	public String getCategoryList() {
		List<Category> list = new ArrayList();
		try {
			String parentId = Struts2Utils.getParameter("parentId");
			String typeKey = Struts2Utils.getParameter("TYPEKEY");
			list = CategoryServiceUtil.getService().getCategoryByParentId(Long.parseLong(parentId), typeKey);
			// map.put("total", list.size());
			// map.put("rows", list);

		} catch (Exception e) {
			// map.put("total", 0);
			// map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("CategoryAction.getCategoryList" + "");
		}
		Struts2Utils.renderDeepJson(list);
		return null;
	}

	public String getCategoryByPrimaryKey() {
		Category model = null;
		try {
			String categoryId = Struts2Utils.getParameter("CATEGORYID");
			model = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(categoryId));

		} catch (Exception e) {
			_log.error("CategoryAction.getCategoryByPrimaryKey" + "");
		}
		Struts2Utils.renderJson(model);
		return null;
	}

	public String getCategoryTree() {
		List retList = new ArrayList();
		try {
			String parentId = Struts2Utils.getParameter("parentId");
			String typeKey = Struts2Utils.getParameter("defkey");
			List<Category> list = CategoryServiceUtil.getService().getCategoryByParentId(Long.parseLong(parentId), typeKey);
			for (Category o : list) {
				HashMap hm = new HashMap();
				hm.put("id", o.getCategoryId());
				hm.put("parentId", o.getParentId());
				List<Category> list2 = CategoryServiceUtil.getService().getCategoryByParentId(o.getCategoryId(), typeKey);
				if (list2 == null || list2.size() == 0) {
					hm.put("leaf", true);
				}
				hm.put("text", o.getName());
				hm.put("typekey", o.getTypeKey());
				retList.add(hm);
			}

		} catch (Exception e) {

			e.printStackTrace();
			_log.error("CategoryAction.getOrganizationTree" + "");
		}
		Struts2Utils.renderDeepJson(retList);
		return null;
	}

}
