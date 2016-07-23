package com.xtsoft.login;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.Constants;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.model.User;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.util.MD5;

public class LoginAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(LoginAction.class);

	public String checkUserAccount() {
		Map map = new HashMap();
		map.put("result", "0");
		try {
			String user = Struts2Utils.getParameter("USERID");
			String password = Struts2Utils.getParameter("PASSWORD");
			Map para = new HashMap();
			para.put("USER", user);

			List<Map> list = UserServiceUtil.getService().findUserList(para, 0, 1);
			if (list != null && list.size() > 0) {
				Map userMap = list.get(0);
				String passwd = (String) userMap.get("PASSWORD");
				if (password != null) {
					if (MD5.toMD5(password).equals(passwd)) {
						map.put("result", "1");
						map.put("msg", "");
						UserShort userShort = new UserShort();
						userShort.setPassword(password);
						userShort.setUserId(Long.parseLong(String.valueOf(userMap.get("USERID"))));
						userShort.setUserName(String.valueOf(userMap.get("USERNAME")));
						userShort.setEmailaddress(String.valueOf(userMap.get("EMAILADDRESS")));
						Struts2Utils.getSession().setAttribute(Constants.CURRENTUSER, userShort);
					} else {
						map.put("msg", "√‹¬Î¥ÌŒÛ");
					}

				} else {
					map.put("msg", "√‹¬Î¥ÌŒÛ");
				}

			} else {
				map.put("msg", "”√ªß≤ª¥Ê‘⁄");
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("LoginAction.String" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String logOut() {
		HttpSession session = Struts2Utils.getSession();
		Map map = new HashMap();
		if (session != null) {
			session.invalidate();
		}
		map.put("msg", "1");
		Struts2Utils.renderJson(map);
		return null;
	}

	public String updateUserPassword() {
		String forward = null;
		int retValue = 1;
		try {
			String oldPassword = Struts2Utils.getParameter("oldPassword");
			String userPW = Struts2Utils.getParameter("userPW");
			UserShort userShort = (UserShort) Struts2Utils.getCurrentUser();
			if (!userShort.getPassword().equals(oldPassword)) {
				retValue = 0;
				userShort.setPassword(userPW);
				Struts2Utils.renderJson(retValue);
				return forward;
			}
			User dbGeUser = UserServiceUtil.getService().findByPrimaryKey(userShort.getUserId());
			String md5Pw = MD5.toMD5(userPW);
			dbGeUser.setPassword(md5Pw);
			UserServiceUtil.getService().update(dbGeUser, null);
			userShort.setPassword(userPW);
			Struts2Utils.getSession().setAttribute(Constants.CURRENTUSER, userShort);
		} catch (Exception ex) {
			retValue = -1;
			_log.error("updateUserPassword", ex);
		}
		Struts2Utils.renderJson(retValue);
		return forward;
	}

}
