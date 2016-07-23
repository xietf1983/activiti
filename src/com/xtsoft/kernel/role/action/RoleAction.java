package com.xtsoft.kernel.role.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.role.RoleServiceUtil;
import com.xtsoft.kernel.role.model.Role;

public class RoleAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(RoleAction.class);

	public String getRoleList() {
		Map map = new HashMap();
		try {
			String name = Struts2Utils.getParameter("NAME");
			HashMap hp = new HashMap();
			if (name != null && !name.equals("")) {
				hp.put("NAME", name);
			}
			List<Map> list = RoleServiceUtil.getService().findRoleList(hp);
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("RoleAction.getRoleList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String editRole() {
		Map map = new HashMap();
		try {
			String name = Struts2Utils.getParameter("NAME");
			String id = Struts2Utils.getParameter("ID");
			String description = Struts2Utils.getParameter("DESCRIPTION");
			Role model = null;
			if (id == null || id.equals("")) {
				model = RoleServiceUtil.getService().create(CounterServiceUtil.increment(Role.class.getName()));
				model.setName(name);
				model.setDescription(description);
			} else {
				model = RoleServiceUtil.getService().getRoleByPrimaryKey(Long.parseLong(id));
				model.setName(name);
				model.setDescription(description);
			}
			RoleServiceUtil.getService().update(model);
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("RoleAction.editRole" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String editRoleMenuList() {
		Map map = new HashMap();
		try {
			String roleId = Struts2Utils.getParameter("ROLEID");
			String ids = Struts2Utils.getParameter("TREEIDS");
			if (ids == null) {
				ids = "";
			}
			RoleServiceUtil.getService().editRoleMenu(Long.parseLong(roleId), ids.split(","));
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("RoleAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	/**
	 * É¾³ýÖ÷Ìå
	 * 
	 * @return
	 */
	public String deleteRole() {
		Map map = new HashMap();
		try {
			String id = Struts2Utils.getParameter("ID");
			Role model = null;
			if (id == null || id.equals("")) {
			} else {
				RoleServiceUtil.getService().removeRole(Long.parseLong(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("RoleAction.deleteRole" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String getRoleMenuList() {
		List<String> list = null;
		try {
			String id = Struts2Utils.getParameter("ID");
			list = RoleServiceUtil.getService().getRoleMenuList(Long.parseLong(id));

		} catch (Exception e) {
			list = new ArrayList();

			_log.error("RoleAction.getRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(list);
		return null;
	}

	public String findUserListByRoleId() {
		Map map = new HashMap();
		try {
			String name = Struts2Utils.getParameter("NAME");
			HashMap hp = new HashMap();
			if (name != null && !name.equals("")) {
				hp.put("USERNAME", "%" + name + "%");
			}
			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			String roleId = Struts2Utils.getParameter("ROLEID");
			String roleIdIn = Struts2Utils.getParameter("ROLEIDIN");
			String roleIdNotIn = Struts2Utils.getParameter("ROLENOTIN");
			hp.put("ROLEID", roleId);
			if (roleIdIn != null && !roleIdIn.equals("")) {
				hp.put("ROLEIN", roleId);
			} else if (roleIdNotIn != null && !roleIdNotIn.equals("")) {
				hp.put("ROLENOTIN", roleId);
			}
			List<Map> list = RoleServiceUtil.getService().findUserListByRoleId(hp, Integer.parseInt(start), Integer.parseInt(limit));
			map.put("total", RoleServiceUtil.getService().findUserCountByRoleId(hp));
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("RoleAction.getRoleList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String editRoleUserList() {
		Map map = new HashMap();
		try {
			String roleId = Struts2Utils.getParameter("ROLEID");
			String userIds = Struts2Utils.getParameter("USERIDS");
			if (userIds == null) {
				userIds = "";
			}
			RoleServiceUtil.getService().editRoleUserList(Long.parseLong(roleId), userIds.split(","));
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("RoleAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String removeRoleUserList() {
		Map map = new HashMap();
		try {
			String roleId = Struts2Utils.getParameter("ROLEID");
			String userIds = Struts2Utils.getParameter("USERIDS");
			if (userIds == null) {
				userIds = "";
			}
			RoleServiceUtil.getService().removeRoleUserList(Long.parseLong(roleId), userIds.split(","));
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("RoleAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

}
