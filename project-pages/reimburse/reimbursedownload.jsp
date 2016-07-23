<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.xtsoft.poi.CellCol"%>
<%@page import="com.xtsoft.poi.ExportEngine"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.xtsoft.category.model.Category"%>
<%@page import="com.xtsoft.category.CategoryServiceUtil"%>

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
		List<HashMap> retlist = new ArrayList();
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
		List<Map> data = com.xtsoft.reimburse.ReimburseServiceUtil.getService().getReimburseStaticList(params);
		if (data == null) {
			data = new ArrayList();
		}
		HashMap totalMap = new HashMap();
		totalMap.put("CREATEDATE_SHOWVALUE", "");
		totalMap.put("CREATEOR_SHOWVALUE", "");
		totalMap.put("COMPANY_SHOWVALUE", "");
		totalMap.put("CATEGORY_SHOWVALUE", "");
		totalMap.put("USERNAME", "合计");
		java.math.BigDecimal totalMout = new java.math.BigDecimal(0);
		for (Map p : data) {
			if (String.valueOf(p.get("STATUS")).equals("5")) {
				p.put("STATUS", "已付款");

			} else if (String.valueOf(p.get("STATUS")).equals("4")) {
				p.put("STATUS", "待付款");

			} else if (String.valueOf(p.get("STATUS")).equals("3")) {
				p.put("STATUS", "退回");

			} else if (String.valueOf(p.get("STATUS")).equals("2")) {
				p.put("STATUS", "待审核");

			} else if (String.valueOf(p.get("STATUS")).equals("1")) {
				p.put("STATUS", "已登记");
			} else {
				p.put("STATUS", "");
			}
			totalMout = totalMout.add(new java.math.BigDecimal(Float.parseFloat(String.valueOf(p.get("AMOUNT")))));
		}
		totalMout = totalMout.setScale(2, java.math.RoundingMode.HALF_UP);
		totalMap.put("AMOUNT", totalMout);
		data.add(totalMap);

		cells = new CellCol[7];
		cells[0] = new CellCol();
		cells[0].setColKey("USERNAME");
		cells[0].setColName("报销人");
		cells[1] = new CellCol();
		cells[1].setColKey("ORGNAME_SHOWVALUE");
		cells[1].setColName("所属合伙人");
		cells[2] = new CellCol();
		cells[2].setColKey("CATEGORY_SHOWVALUE");
		cells[2].setColName("报销类别");
		cells[3] = new CellCol();
		cells[3].setColKey("COMPANY_SHOWVALUE");
		cells[3].setColName("报销主体");
		cells[4] = new CellCol();
		cells[4].setColKey("AMOUNT");
		cells[4].setColName("报销金额");
		cells[5] = new CellCol();
		cells[5].setColKey("CREATEDATE_SHOWVALUE");
		cells[5].setColName("报销日期");
		cells[6] = new CellCol();
		cells[6].setColKey("STATUS");
		cells[6].setColName("报销状态");

		params.put("DefaultHeaderHeight", "800");
		params.put("DefaultColumnWidth", "20");
		params.put("sheetName", "报销统计");
		params.put("fileName", "报销统计");
		params.put("header", "报销统计" + startTime + "至" + endTime);
		new com.xtsoft.poi.ExportEngine().exportCommData(params, cells, data, response);
		out.clear();
		out = pageContext.pushBody();
	%>
</body>
</html>