package com.xtsoft.kernel.user.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.model.User;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.util.MD5;

public class UserAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(UserAction.class);

	public String getUserList() {
		Map map = new HashMap();
		Map para = new HashMap();
		try {
			String keyName = Struts2Utils.getParameter("KEYNAME");
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			String status = Struts2Utils.getParameter("STATUS");
			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			if (keyName != null && !keyName.equals("")) {
				para.put("KEYNAME", "%" + keyName + "%");
			}
			if (organizationId != null && !organizationId.equals("") && !organizationId.equals("0")) {
				para.put("ORGANIZATIONID", organizationId);
			}
			if (status != null && !status.equals("")) {
				para.put("STATUS", status);
			}
			map.put("total", UserServiceUtil.getService().findUserCount(para));
			map.put("rows", UserServiceUtil.getService().findUserList(para, Integer.parseInt(start), Integer.parseInt(limit)));

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("UserAction.getUserList" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String getUserSelectedOrgList() {
		Map map = new HashMap();
		try {
			String userId = Struts2Utils.getParameter("USERID");
			if (userId == null && userId.equals("")) {
				userId = "-1";
			}
			List<HashMap> list = UserServiceUtil.getService().getOrgIds(Long.parseLong(userId));
			if (list == null) {
				list = new ArrayList();
			}
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("UserAction.getUserList" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String getUserOrgList() {
		Map map = new HashMap();
		try {
			UserShort usershort = Struts2Utils.getCurrentUser();
			List<HashMap> list = UserServiceUtil.getService().getOrgIds(usershort.getUserId());
			if (list == null) {
				list = new ArrayList();
			}
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("UserAction.getUserList" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String editUser() {
		Map map = new HashMap();
		User model;
		try {
			String userId = Struts2Utils.getParameter("USERID");
			String userName = Struts2Utils.getParameter("NAME");
			String description = Struts2Utils.getParameter("DESCRIPTION");
			String emailAddress = Struts2Utils.getParameter("EMAILADDRESS");
			String password = Struts2Utils.getParameter("PASSWORD");
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			// 业务部门
			String organizationIds = Struts2Utils.getParameter("ORGIDS");
			// 人员岗位
			if (organizationIds == null) {
				organizationIds = "";
			}
			String userType = Struts2Utils.getParameter("USERTYPE");
			String tel = Struts2Utils.getParameter("TEL");
			String status = Struts2Utils.getParameter("STATUS");
			if (userId == null || userId.equals("")) {
				model = UserServiceUtil.getService().create(CounterServiceUtil.increment(User.class.getName()));
				model.setStatus(1);
				model.setTel(tel);
				model.setUserName(userName);
				model.setOrganizationId(organizationId);
				model.setPassword(MD5.toMD5(password));
				model.setEmailAddress(emailAddress);
				model.setCreateDate(new Date());
				model.setDescription(description);
				model.setModifiedDate(new Date());
				model.setUserType(Integer.parseInt(userType));

			} else {
				model = UserServiceUtil.getService().findByPrimaryKey(Long.parseLong(userId));
				model.setTel(tel);
				model.setStatus(1);
				model.setUserName(userName);
				model.setOrganizationId(organizationId);
				if (model.getPassword().equals(password)) {
					model.setPassword(password);
				} else {
					model.setPassword(MD5.toMD5(password));
				}
				// model.setPassword(password);
				model.setEmailAddress(emailAddress);
				model.setCreateDate(new Date());
				model.setDescription(description);
				model.setModifiedDate(new Date());
				if (status != null && !status.equals("")) {
					model.setStatus(Integer.parseInt(status));
				}
				model.setUserType(Integer.parseInt(userType));
			}
			UserServiceUtil.getService().update(model, organizationIds.split(","));

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("UserAction.editUser" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String editUserStatus() {
		Map map = new HashMap();
		User model;
		try {
			String userId = Struts2Utils.getParameter("USERID");
			String status = Struts2Utils.getParameter("STATUS");
			model = UserServiceUtil.getService().findByPrimaryKey(Long.parseLong(userId));
			if (status != null && !status.equals("")) {
				model.setStatus(Integer.parseInt(status));
			}
			UserServiceUtil.getService().update(model, null);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("UserAction.editUser" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String getCurrentUser() {
		UserShort currentUser = null;
		try {
			currentUser = Struts2Utils.getCurrentUser();

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("UserAction.getCurrentUser" + "");
		}
		Struts2Utils.renderDeepJson(currentUser);
		return null;

	}
}
