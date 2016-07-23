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
			responses.reset();// 清空输出流
			responses.setHeader("Content-disposition", "attachment; filename=" + (new Date()).getTime() + ".xls");// 设定输出文件头
			responses.setContentType("application/msexcel");// 定义输出类型
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建字体对象
			HSSFFont font = workbook.createFont();
			// 加粗
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 字体用什么
			font.setFontName("黑体 ");
			// 单元格样式对象
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			// 单元格的字体用什么？就用上面设置好的东西
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFSheet sheet = workbook.createSheet(sheetName);
			sheet.setDefaultColumnWidth((short) 12);
			// 生成表头
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
			// 生成数据
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
			iLog.info("成功导出Excel!");
			// 写入文件
		} catch (Exception ex) {
			iLog.error("导出Excel出错了", ex);
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				iLog.error("关闭os失败", ex);
			}
		}
	}

	public void exportCommData(Map map, CellCol[] headertxt, List<Map> list, HttpServletResponse responses) {
		OutputStream os = null;
		try {
			os = responses.getOutputStream();
			responses.reset();// 清空输出流
			String fileName = map.get("fileName").toString();
			fileName = toUtf8String(fileName);
			responses.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");// 设定输出文件头
			responses.setContentType("application/msexcel");// 定义输出类型
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFFont font = workbook.createFont();// 创建字体对象
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			font.setFontName("黑体 ");// 设置字体
			// Sheet名称
			HSSFSheet sheet = workbook.createSheet(map.get("sheetName").toString());
			if (map.get("DefaultColumnWidth") != null) {
				sheet.setDefaultColumnWidth(Short.parseShort(map.get("DefaultColumnWidth") + ""));// 默认列宽
			} else {
				sheet.setDefaultColumnWidth((short) 14);// 默认列宽
			}
			// 合并单元格,表头说明信息
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headertxt.length - 1));
			HSSFCellStyle cellStyle2 = setHeadRegionStyle(workbook);// 设置合并头部单元格样式
			// 创建第一行
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
			HSSFCellStyle cellStyle = setRowsRegionStyle(workbook);// 设置合并行单元格样式
			// 创建第二行，生成表头
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
			HSSFCellStyle cs = setCommonStyle(workbook);// 设置普通单元格样式
			// 生成数据
			for (int i = 0; i < list.size(); i++) {
				HSSFRow datarow = sheet.createRow(i + 2);
				Map mp = list.get(i);
				for (short k = 0; k < headertxt.length; k++) {
					HSSFCell datacell = datarow.createCell(k);
					HSSFDataFormat format = workbook.createDataFormat(); 
					//cs.setDataFormat(format.getFormat("@"));
					datacell.setCellStyle(cs);
					//HSSFDataFormat format = workbook.createDataFormat(); 
					//这样才能真正的控制单元格格式，@就是指文本型，具体格式的定义还是参考上面的原文吧 
					// cellStyle.setDataFormat(format.getFormat("@")); 
					datacell.setCellType(Cell.CELL_TYPE_STRING);
					datacell.setCellValue((mp.get(headertxt[k].getColKey()) == null ? "'" : ""+ mp.get(headertxt[k].getColKey()) + " "));
				}
			}
			workbook.write(os);
			iLog.info("成功导出Excel!");
			// 写入文件
		} catch (Exception ex) {
			iLog.error("导出Excel出错了", ex);
		} finally {
			try {
				os.close();
			} catch (Exception ex) {
				iLog.error("关闭os失败", ex);
			}
		}
	}
	// 设置头部合并单元格样式
		private HSSFCellStyle setHeadRegionStyle(HSSFWorkbook workbook) {
			HSSFCellStyle cellStyle = setCommonStyle(workbook);
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);// 字体大小
			font.setFontName("宋体");// 字体类型
			cellStyle.setFont(font);
			cellStyle.setWrapText(true);
			return cellStyle;
		}

		// 设置行合并单元格样式
		private HSSFCellStyle setRowsRegionStyle(HSSFWorkbook workbook) {
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
			font.setFontName("黑体");
			HSSFCellStyle cellStyle = setCommonStyle(workbook);
			cellStyle.setFont(font);
			return cellStyle;
		}

		// 设置行普通单元格样式
		private HSSFCellStyle setCommonStyle(HSSFWorkbook workbook) {
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			return cellStyle;
		}

		// 把字符串转成utf8编码，保证中文文件名不会乱码
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
