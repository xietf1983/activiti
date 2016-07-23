package com.xtsoft.kernel.menu.action;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.kernel.menu.MenuServiceUtil;
import com.xtsoft.kernel.menu.model.Menu;
import com.xtsoft.kernel.user.model.UserShort;

public class MenuAction extends ActionSupport {
	private static Log _log = LogFactory.getLog(MenuAction.class);

	public void getMenuByUserIdAndParent() {
		UserShort usershort = Struts2Utils.getCurrentUser();
		if (usershort == null) {
			usershort = new UserShort();
			usershort.setUserId(1);
		}
		try {
			List<Menu> list = MenuServiceUtil.getService().getMenuByUserIdAndParent(usershort.getUserId(), 0);
			if (list != null && list.size() > 0) {
				for (Menu m : list) {
					List<Menu> secondList = MenuServiceUtil.getService().getMenuByUserIdAndParent(usershort.getUserId(), m.getId());
					StringBuffer b = new StringBuffer();
					for (Menu s : secondList) {
						b.append("<li class='listmenu' ><a href=\"javascript:addTomainTab('");
						b.append(s.getText());
						b.append("','");
						b.append(s.getTargeturl());
						b.append("','");
						b.append(s.getTargeturl());
						b.append("',true)\">");
						b.append(s.getText());
						b.append("</a>");
						b.append("</li>");
					}
					m.setDescription(b.toString());
				}
				Struts2Utils.renderDeepJson(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("MenuAction.getMenuByUserIdAndParent" + "");
		}

	}

	public void getMenuByParent() {
		List<Menu> list = null;
		try {
			Menu menu = new Menu();
			menu.setId(0);
			list = MenuServiceUtil.getService().getMenuByParent(menu);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("MenuAction.getMenuByUserIdAndParent" + "");
		}
		Struts2Utils.renderDeepJson(list);
	}

}
