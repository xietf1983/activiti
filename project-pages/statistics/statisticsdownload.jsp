<%@page import="com.xtsoft.kernel.organization.model.Organization"%>
<%@page import="com.xtsoft.kernel.company.model.Company"%>
<%@page import="java.math.*"%>
<%@page import="com.xtsoft.analyse.model.AnalyseModel"%>
<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xtsoft.poi.CellCol"%>
<%@page import="com.xtsoft.poi.ExportEngine"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.xtsoft.analyse.AnalyseServiceUtil"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xtsoft.category.model.Category"%>
<%@page import="com.xtsoft.category.CategoryServiceUtil"%>
<%@page import="com.xtsoft.kernel.organization.*"%>
<%@page import="com.xtsoft.kernel.company.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title>报销导出</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<body>
	<%
		Map mp = new HashMap();
		Map params = new HashMap();
		List<HashMap> list = null;
		List<HashMap> roadlist = null;
		List<Map> retlist = new ArrayList();
		CellCol[] cells = null;
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String companyId = request.getParameter("companyId");
		String type = request.getParameter("type");
		String orgIds = request.getParameter("orgIds");
		String status = request.getParameter("status");
		String organizationId = request.getParameter("organizationId");
		if (startTime != null && !startTime.equals("")) {
			params.put("STARTDAY", startTime);
		}
		if (orgIds != null && !orgIds.equals("")) {
			params.put("ORGANIZATIONIDS", orgIds);
		}
		if (orgIds != null && !orgIds.equals("")) {
			params.put("ORGANIZATIONID", orgIds);
		}
		if (endTime != null && !endTime.equals("")) {
			params.put("ENDDAY", endTime);
		}
		if (status != null && !status.equals("")) {
			params.put("STATUS", status);
		}
		if (companyId != null && !companyId.equals("")) {
			params.put("COMPANYID", companyId);
		}

		if (type != null && !type.equals("") && !type.equals("0")) {
			Category c = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(type));
			if (c != null) {
				List<Category> categoryList = CategoryServiceUtil.getService().findByFullPath(c.getFullPath());
				StringBuffer categoryBuffer = new StringBuffer();
				for (int j = 0; j < categoryList.size(); j++) {
					if (j == 0) {
						categoryBuffer.append(categoryList.get(j).getCategoryId());
					} else {
						categoryBuffer.append(",");
						categoryBuffer.append(categoryList.get(j).getCategoryId());
					}
				}
				if (!categoryBuffer.toString().equals("")) {
					params.put("TYPES", categoryBuffer.toString());
				}
			}
		}
		com.xtsoft.kernel.user.model.UserShort currentUser = (com.xtsoft.kernel.user.model.UserShort) request.getSession().getAttribute(com.xtsoft.Constants.CURRENTUSER);
		if (organizationId != null && !organizationId.equals("")) {
			params.put("ORGANIZATIONID", organizationId);
		}
		StringBuffer orgbuffer = new StringBuffer();
		List<HashMap> listMap = com.xtsoft.kernel.user.UserServiceUtil.getService().getOrgIds(currentUser.getUserId());
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
		params.put("ORGIDS", orgbuffer.toString());
		List<Map> reimburseList = AnalyseServiceUtil.getService().findReimburseAnalyseList(params);
		List<Map> appropriateList = AnalyseServiceUtil.getService().findAppropriateAnalyseList(params);
		Map<String, AnalyseModel> data = new HashMap();
		if (reimburseList != null && reimburseList.size() > 0) {
			for (Map p : reimburseList) {
				String key = p.get("ORGANIZATIONID") + "-" + p.get("COMPANYID");
				AnalyseModel model = new AnalyseModel();
				model.setCompanyId(Long.parseLong(String.valueOf(p.get("COMPANYID"))));
				model.setOrganizationId(Long.parseLong(String.valueOf(p.get("ORGANIZATIONID"))));
				Company c = CompanyServiceUtil.getService().getCompanyByPrimaryKey(model.getCompanyId());
				model.setCompanyName(c.getName());
				Organization org = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(model.getOrganizationId());
				model.setOrganizationName(org.getText());
				model.setReimburseamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
				model.setBalance(-new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
				data.put(key, model);
			}
		}
		if (appropriateList != null && appropriateList.size() > 0) {
			for (Map p : appropriateList) {
				String key = p.get("ORGANIZATIONID") + "-" + p.get("COMPANYID");
				if (data.get(key) != null) {
					AnalyseModel model = data.get(key);
					model.setAppropriateamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					model.setBalance(new BigDecimal(model.getBalance() + Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					data.put(key, model);
				} else {
					AnalyseModel model = new AnalyseModel();
					model.setCompanyId(Long.parseLong(String.valueOf(p.get("COMPANYID"))));
					model.setOrganizationId(Long.parseLong(String.valueOf(p.get("ORGANIZATIONID"))));
					Company c = CompanyServiceUtil.getService().getCompanyByPrimaryKey(model.getCompanyId());
					model.setCompanyName(c.getName());
					Organization org = OrganizationServiceUtil.getService().getOrganizationByPrimaryKey(model.getOrganizationId());
					model.setOrganizationName(org.getText());
					model.setAppropriateamount(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					model.setBalance(new BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))).setScale(2, RoundingMode.HALF_UP).floatValue());
					data.put(key, model);
				}
			}
		}
		Set<String> key = data.keySet();
		List<AnalyseModel> dataList = new ArrayList();
		AnalyseModel total = new AnalyseModel();
		total.setCompanyName("合计");
		for (Iterator it = key.iterator(); it.hasNext();) {
			String s = (String) it.next();
			AnalyseModel analyse = data.get(s);
			dataList.add(analyse);
			total.setAppropriateamount(new BigDecimal(total.getAppropriateamount()).add(new BigDecimal(analyse.getAppropriateamount())).floatValue());
			total.setBalance(new BigDecimal(total.getBalance()).add(new BigDecimal(analyse.getBalance())).floatValue());
			total.setReimburseamount(new BigDecimal(total.getReimburseamount()).add(new BigDecimal(analyse.getReimburseamount())).floatValue());
		}
		total.setAppropriateamount(new BigDecimal(total.getAppropriateamount()).setScale(2, RoundingMode.HALF_UP).floatValue());
		total.setBalance(new BigDecimal(total.getBalance()).setScale(2, RoundingMode.HALF_UP).floatValue());
		total.setReimburseamount(new BigDecimal(total.getReimburseamount()).setScale(2, RoundingMode.HALF_UP).floatValue());
		dataList.add(total);
		for (AnalyseModel d : dataList) {
			Map model = new HashMap();
			model.put("companyName", d.getCompanyName());
			model.put("organizationName", d.getOrganizationName());
			model.put("appropriateamount", d.getAppropriateamount());
			model.put("reimburseamount", d.getReimburseamount());
			model.put("balance", d.getBalance());
			retlist.add(model);
		}
		cells = new CellCol[5];
		cells[1] = new CellCol();
		cells[1].setColKey("companyName");
		cells[1].setColName("所属主体");
		cells[0] = new CellCol();
		cells[0].setColKey("organizationName");
		cells[0].setColName("所属合伙人");
		cells[2] = new CellCol();
		cells[2].setColKey("appropriateamount");
		cells[2].setColName("划拨金额");
		cells[3] = new CellCol();
		cells[3].setColKey("reimburseamount");
		cells[3].setColName("报销金额");
		cells[4] = new CellCol();
		cells[4].setColKey("balance");
		cells[4].setColName("结存金额");

		params.put("DefaultHeaderHeight", "800");
		params.put("DefaultColumnWidth", "20");
		params.put("sheetName", "报销，划拨综合统计");
		params.put("fileName", "报销，划拨综合统计");
		params.put("header", "报销/划拨综合统计" + startTime + "至" + endTime);
		new com.xtsoft.poi.ExportEngine().exportCommData(params, cells, retlist, response);
		out.clear();
		out = pageContext.pushBody();
	%>
</body>
</html>