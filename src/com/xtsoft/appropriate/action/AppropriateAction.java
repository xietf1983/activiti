package com.xtsoft.appropriate.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.appropriate.AppropriateServiceUtil;
import com.xtsoft.appropriate.model.Appropriate;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.reimburse.ReimburseServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.util.DateUtil;

/**
 * 划拨登记，导入
 * 
 * @author xietf
 * 
 */
public class AppropriateAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(AppropriateAction.class);

	public String getAppropriateList() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			String startTime = Struts2Utils.getParameter("STARTTIME");
			String endTime = Struts2Utils.getParameter("ENDTIME");
			String companyId = Struts2Utils.getParameter("COMPANYID");
			String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
			UserShort currentUser = Struts2Utils.getCurrentUser();
			if (startTime != null && !startTime.equals("")) {
				hp.put("STARTDAY", startTime);
			}

			if (endTime != null && !endTime.equals("")) {
				hp.put("ENDDAY", endTime);
			}
			if (companyId != null && !companyId.equals("")) {
				hp.put("COMPANYID", companyId);
			}
			if (organizationId != null && !organizationId.equals("")) {
				hp.put("ORGANIZATIONIDS", organizationId);
			}
			List<HashMap> listMap = UserServiceUtil.getService().getOrgIds(currentUser.getUserId());
			StringBuffer orgbuffer = new StringBuffer();
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
			map.put("total", AppropriateServiceUtil.getService().findAppropriateCount(hp));
			map.put("rows", AppropriateServiceUtil.getService().findAppropriateList(hp, Integer.parseInt(start), Integer.parseInt(limit)));

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("AppropriateAction.getAppropriateList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String loadAppropriate() {
		Appropriate model = null;
		try {
			String appropriateId = Struts2Utils.getParameter("APPROPRIATEID");
			if (appropriateId == null || !appropriateId.equals("")) {
				model = AppropriateServiceUtil.getService().findByPrimaryKey(Long.parseLong(appropriateId));

			}

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("AppropriateAction.loadAppropriate" + "");
		}
		Struts2Utils.renderDeepJson(model);
		return null;
	}

	public String editAppropriate() {
		Map map = new HashMap();
		String appropriateId = Struts2Utils.getParameter("APPROPRIATEID");
		String amount = Struts2Utils.getParameter("AMOUNT");
		String organizationId = Struts2Utils.getParameter("ORGANIZATIONID");
		String description = Struts2Utils.getParameter("DESCRIPTION");
		String appropriateDate = Struts2Utils.getParameter("APPROPRIATEDATE");
		String companyId = Struts2Utils.getParameter("COMPANYID");
		UserShort usershort = Struts2Utils.getCurrentUser();
		try {
			Appropriate model = null;
			if (appropriateId == null || appropriateId.equals("")) {
				model = AppropriateServiceUtil.getService().create(CounterServiceUtil.increment(Appropriate.class.getName()));
				model.setActive(1);
				model.setAmount(Float.parseFloat(amount));
				model.setCreateor(usershort.getUserId());
				model.setCompanyId(Long.parseLong(companyId));
				model.setAppropriateDate(DateUtil.getDateFromString(appropriateDate));
				model.setCreateDate(new Date());
				model.setOrganizationId(Long.parseLong(organizationId));
				model.setDescription(description);

			} else {
				model = AppropriateServiceUtil.getService().findByPrimaryKey(Long.parseLong(appropriateId));
				model.setAmount(Float.parseFloat(amount));
				model.setCreateor(usershort.getUserId());
				model.setCompanyId(Long.parseLong(companyId));
				model.setAppropriateDate(DateUtil.getDateFromString(appropriateDate));
				model.setCreateDate(new Date());
				model.setOrganizationId(Long.parseLong(organizationId));
				model.setDescription(description);

			}
			AppropriateServiceUtil.getService().update(model);

		} catch (Exception e) {
			e.printStackTrace();
			_log.error("AppropriateAction.editAppropriate" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String removeAppropriate() {
		Map map = new HashMap();
		try {
			HashMap hp = new HashMap();
			String appropriateId = Struts2Utils.getParameter("APPROPRIATEID");
			AppropriateServiceUtil.getService().removeAppropriate(Long.parseLong(appropriateId));
			map.put("msg", "1");
		} catch (Exception e) {
			map.put("msg", "0");
			_log.error("ReimburseAction.removeAppropriate" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String addAppropriateFromJsonList() {
		Map map = new HashMap();
		UserShort usershort = Struts2Utils.getCurrentUser();
		String jsonList = Struts2Utils.getParameter("JSONLIST");
		try {
			List<Appropriate> list = AppropriateServiceUtil.buildAppropriateListFromJson(jsonList, usershort.getUserId());
			AppropriateServiceUtil.getService().addAppropriateList(list);
			map.put("msg", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "0");
			_log.error("AppropriateAction.addAppropriateFromJsonList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

}
