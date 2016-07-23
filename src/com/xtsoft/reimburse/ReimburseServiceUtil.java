package com.xtsoft.reimburse;

import java.util.ArrayList;
import java.util.List;

import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.reimburse.model.ReimburseDetail;
import com.xtsoft.reimburse.service.ReimburseService;

import net.sf.json.JSONArray;

public class ReimburseServiceUtil {
	private static ReimburseService _service;

	public static ReimburseService getService() {
		if (_service == null) {
			throw new RuntimeException("_service is not set");
		}

		return _service;
	}

	public void setService(ReimburseService service) {
		_service = service;
	}

	public static List<ReimburseDetail> buildReimburseDetailListFromJson(String jsonString, long reimburseId, long userId) {
		List<ReimburseDetail> list = new ArrayList();
		try {
			JSONArray jsonArray = JSONArray.fromObject(jsonString);
			for (int i = 0; i < jsonArray.size(); i++) {
				net.sf.json.JSONObject obj = jsonArray.getJSONObject(i);
				ReimburseDetail r = ReimburseServiceUtil.getService().createReimburseDetail(CounterServiceUtil.increment(ReimburseDetail.class.getName()));
				r.setActive(1);
				r.setCreateUserId(userId);
				r.setReimburseId(reimburseId);
				r.setAmount(Float.parseFloat(obj.getString("AMOUNT")));
				r.setAppropriateUserId(Long.parseLong(obj.getString("USERID")));
				r.setOrganizationId(Long.parseLong(obj.getString("ORGANIZATIONID")));
				r.setBillnumber(Integer.parseInt(obj.getString("BILLNUMBER")));
				r.setDescription(obj.getString("DESCRIPTION"));
				list.add(r);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}
