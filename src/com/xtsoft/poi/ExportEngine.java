package com.xtsoft.poi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportEngine {
	private static Logger iLog = Logger.getLogger(ExportEngine.class.getName());

	public static void exportData(String sheetName, CellCol[] headertxt, List<Map> list, HttpServletResponse responses) {
		OutputStream os = null;
		try {
			os = responses.getOutputStream();
			responses.reset();// ��������
			responses.setHeader("Content-disposition", "attachment; filename=" + (new Date()).getTime() + ".xls");// �趨����ļ�ͷ
			responses.setContentType("application/msexcel");// �����������
			HSSFWorkbook workbook = new HSSFWorkbook();
			// �����������
			HSSFFont font = workbook.createFont();
			// �Ӵ�
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// ������ʲô
			font.setFontName("���� ");
			// ��Ԫ����ʽ����
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			// ��Ԫ���������ʲô�������������úõĶ���
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet sheet = workbook.createSheet(sheetName);
			sheet.setDefaultColumnWidth((short) 12);
			// ���ɱ�ͷ
			HSSFRow row = sheet.createRow((short) 0);
			if (headertxt != null && headertxt.length > 0) {
				short j = 0;
				for (CellCol s : headertxt) {
					HSSFCell cell1 = row.createCell(j);
					cell1.setCellValue(headertxt[j].getColName());
					cell1.setCellStyle(cellStyle);
					j = (short) (j + 1);
				}

			}
			HSSFCellStyle cs = workbook.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// ��������
			for (int i = 0; i < list.size(); i++) {
				HSSFRow datarow = sheet.createRow(i + 1);
				Map mp = list.get(i);
				for (short k = 0; k < headertxt.length; k++) {
					HSSFCell datacell = datarow.createCell(k);
					datacell.setCellStyle(cs);
					datacell.setCellValue(mp.get(headertxt[k].getColKey()) == null ? "" : mp.get(headertxt[k].getColKey()) + "");
				}
			}
			workbook.write(os);
			iLog.info("�ɹ�����Excel!");
			// д���ļ�
		} catch (Exception ex) {
			iLog.error("����Excel������", ex);
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				iLog.error("�ر�osʧ��", ex);
			}
		}
	}

	public void exportCommData(Map map, CellCol[] headertxt, List<Map> list, HttpServletResponse responses) {
		OutputStream os = null;
		try {
			os = responses.getOutputStream();
			responses.reset();// ��������
			String fileName = map.get("fileName").toString();
			fileName = toUtf8String(fileName);
			responses.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");// �趨����ļ�ͷ
			responses.setContentType("application/msexcel");// �����������
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFFont font = workbook.createFont();// �����������
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// �Ӵ�
			font.setFontName("���� ");// ��������
			// Sheet����
			HSSFSheet sheet = workbook.createSheet(map.get("sheetName").toString());
			if (map.get("DefaultColumnWidth") != null) {
				sheet.setDefaultColumnWidth(Short.parseShort(map.get("DefaultColumnWidth") + ""));// Ĭ���п�
			} else {
				sheet.setDefaultColumnWidth((short) 14);// Ĭ���п�
			}
			// �ϲ���Ԫ��,��ͷ˵����Ϣ
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headertxt.length - 1));
			HSSFCellStyle cellStyle2 = setHeadRegionStyle(workbook);// ���úϲ�ͷ����Ԫ����ʽ
			// ������һ��
			HSSFRow row2 = sheet.createRow(0);
			row2.setHeight((short) 400);
			if (map.get("DefaultHeaderHeight") != null) {
				row2.setHeight((short) Short.parseShort(map.get("DefaultHeaderHeight") + ""));
			}
			HSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(map.get("header").toString());
			cell2.setCellStyle(cellStyle2);
			for (int k = 1; k < headertxt.length; k++) {
				row2.createCell(k).setCellStyle(cellStyle2);
			}
			HSSFCellStyle cellStyle = setRowsRegionStyle(workbook);// ���úϲ��е�Ԫ����ʽ
			// �����ڶ��У����ɱ�ͷ
			HSSFRow row = sheet.createRow(1);
			if (headertxt != null && headertxt.length > 0) {
				short j = 0;
				for (CellCol s : headertxt) {
					HSSFCell cell1 = row.createCell(j);
					cell1.setCellValue(headertxt[j].getColName());
					cell1.setCellStyle(cellStyle);
					if (headertxt[j].getWidth() != null && headertxt[j].getWidth() > 0) {
						sheet.setColumnWidth(j, headertxt[j].getWidth());
					}
					j = (short) (j + 1);
				}
			}
			HSSFCellStyle cs = setCommonStyle(workbook);// ������ͨ��Ԫ����ʽ
			// ��������
			for (int i = 0; i < list.size(); i++) {
				HSSFRow datarow = sheet.createRow(i + 2);
				Map mp = list.get(i);
				for (short k = 0; k < headertxt.length; k++) {
					HSSFCell datacell = datarow.createCell(k);
					HSSFDataFormat format = workbook.createDataFormat(); 
					//cs.setDataFormat(format.getFormat("@"));
					datacell.setCellStyle(cs);
					//HSSFDataFormat format = workbook.createDataFormat(); 
					//�������������Ŀ��Ƶ�Ԫ���ʽ��@����ָ�ı��ͣ������ʽ�Ķ��廹�ǲο������ԭ�İ� 
					// cellStyle.setDataFormat(format.getFormat("@")); 
					datacell.setCellType(Cell.CELL_TYPE_STRING);
					datacell.setCellValue((mp.get(headertxt[k].getColKey()) == null ? "'" : ""+ mp.get(headertxt[k].getColKey()) + " "));
				}
			}
			workbook.write(os);
			iLog.info("�ɹ�����Excel!");
			// д���ļ�
		} catch (Exception ex) {
			iLog.error("����Excel������", ex);
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				iLog.error("�ر�osʧ��", ex);
			}
		}
	}
	// ����ͷ���ϲ���Ԫ����ʽ
		private HSSFCellStyle setHeadRegionStyle(HSSFWorkbook workbook) {
			HSSFCellStyle cellStyle = setCommonStyle(workbook);
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);// �����С
			font.setFontName("����");// ��������
			cellStyle.setFont(font);
			cellStyle.setWrapText(true);
			return cellStyle;
		}

		// �����кϲ���Ԫ����ʽ
		private HSSFCellStyle setRowsRegionStyle(HSSFWorkbook workbook) {
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// �Ӵ�
			font.setFontName("����");
			HSSFCellStyle cellStyle = setCommonStyle(workbook);
			cellStyle.setFont(font);
			return cellStyle;
		}

		// ��������ͨ��Ԫ����ʽ
		private HSSFCellStyle setCommonStyle(HSSFWorkbook workbook) {
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// ��߿�
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// �ϱ߿�
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// �ұ߿�
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// �±߿�
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ˮƽ����
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// ��ֱ����
			return cellStyle;
		}

		// ���ַ���ת��utf8���룬��֤�����ļ�����������
		public static String toUtf8String(String s) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					sb.append(c);
				} else {
					byte[] b;
					try {
						b = Character.toString(c).getBytes("utf-8");
					} catch (Exception ex) {
						b = new byte[0];
					}
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
			return sb.toString();
		}

}
