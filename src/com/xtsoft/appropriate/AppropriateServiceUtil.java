package com.xtsoft.appropriate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xtsoft.appropriate.model.Appropriate;
import com.xtsoft.appropriate.service.AppropriateService;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.util.DateUtil;

import net.sf.json.JSONArray;

public class AppropriateServiceUtil {

	private static AppropriateService _service;

	public static AppropriateService getService() {
		if (_service == null) {
			throw new RuntimeException("_service is not set");
		}

		return _service;
	}

	public void setService(AppropriateService service) {
		_service = service;
	}

	public static List<Appropriate> buildAppropriateListFromJson(String jsonString, long userId) {
		List<Appropriate> list = new ArrayList();
		try {
			JSONArray jsonArray = JSONArray.fromObject(jsonString);
			for (int i = 0; i < jsonArray.size(); i++) {
				net.sf.json.JSONObject obj = jsonArray.getJSONObject(i);
				Appropriate r = AppropriateServiceUtil.getService().create(CounterServiceUtil.increment(Appropriate.class.getName()));
				r.setActive(1);
				r.setCreateor(userId);
				r.setAmount(Float.parseFloat(obj.getString("AMOUNT")));
				r.setCompanyId(Long.parseLong(obj.getString("COMPANYID")));
				r.setOrganizationId(Long.parseLong(obj.getString("ORGANIZATIONID")));
				r.setAppropriateDate(DateUtil.getDateFromString(obj.getString("APPROPRIATEDATE")));
				r.setCreateDate(new Date());
				r.setDescription(obj.getString("DESCRIPTION"));
				list.add(r);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}
