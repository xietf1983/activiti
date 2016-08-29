package com.xtsoft.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.struts.Struts2Utils;
import com.xtsoft.kernel.company.CompanyServiceUtil;
import com.xtsoft.kernel.company.model.Company;
import com.xtsoft.kernel.organization.OrganizationServiceUtil;
import com.xtsoft.kernel.organization.model.Organization;
import com.xtsoft.kernel.user.model.UserShort;

public class ValidateEngine {
	/**
	 * 批量导入
	 * 
	 * @param inputStream
	 * @return
	 */
	public static List<Map> validateAppropriate(InputStream inputStream, boolean xlsx, UserShort usershort) {
		List<Map> retList = new ArrayList();
		Workbook book = null;
		try {
			if (xlsx) {
				book = new XSSFWorkbook(inputStream);
			} else {
				book = new HSSFWorkbook(inputStream);

			}
			Sheet sheet = book.getSheetAt(0);
			// Iterator<Row> rows = sheet.rowIterator();
			BigDecimal totalMout = new BigDecimal(0);
			int last = sheet.getLastRowNum();
			for (int i = 1; i <= last; i++) {
				Row row = sheet.getRow(i);
				Map dataMap = new HashMap();

				dataMap.put("CREATEOR", usershort.getUserId());
				dataMap.put("CREATEOR_SHOWVALUE", usershort.getUserName());
				StringBuffer errorMsg = new StringBuffer();
				String orgName = row.getCell(0).getStringCellValue();
				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
				if (orgName == null) {
					orgName = "";
				}
				Organization model = OrganizationServiceUtil.getService().getOrganizationByName(orgName);
				if (model != null) {
					dataMap.put("ORGANIZATIONID", model.getOrganizationId());
					dataMap.put("ORGNAME_SHOWVALUE", model.getText());
				} else {
					errorMsg.append(" 所属于合伙人不存在");
				}
				String companyName = row.getCell(1).getStringCellValue();
				if (companyName == null) {
					companyName = "";
				}
				Company c = CompanyServiceUtil.getService().findByName(companyName);
				if (c != null) {
					dataMap.put("COMPANYID", c.getCompanyId());
					dataMap.put("COMPANY_SHOWVALUE", c.getName());
				} else {
					errorMsg.append(" 所属主体不存在");
				}
				String amount = row.getCell(2).getStringCellValue();
				try {
					Float.parseFloat(amount);
					dataMap.put("AMOUNT", amount);
				} catch (Exception ex) {
					errorMsg.append(" 金额不是有效的数字");
				}
				if (row.getCell(4) != null) {
					String description = row.getCell(4).getStringCellValue();
					if (description != null) {
						dataMap.put("DESCRIPTION", description);
					}
				}
				String appropriateDate = row.getCell(3).getStringCellValue();
				try {
					Date date = DateUtil.getDateFromString(appropriateDate);
					if (date == null) {
						errorMsg.append(" 划拨不是有效的日期格式");
					} else {
						dataMap.put("APPROPRIATEDATE_SHOWVALUE", appropriateDate);
					}
				} catch (Exception ex) {
					errorMsg.append(" 划拨不是有效的日期格式");
				}
				dataMap.put("MSG", errorMsg.toString());
				if (errorMsg.toString().equals("")) {
					dataMap.put("SUCESS", "1");
					totalMout = totalMout.add(new BigDecimal(Float.parseFloat(amount)));
				} else {
					dataMap.put("SUCESS", "0");
				}
				retList.add(dataMap);

			}

		} catch (Exception ex) {

		}
		return retList;

	}
}
