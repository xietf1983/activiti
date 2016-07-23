package com.xtsoft.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * 系统需要用到的日期、时间处理类(如果框架中有，首先使用框架中的)
 * 
 * 
 */
public class DateUtil {
	public static final long SECOND = 1000;

	public static final long MINUTE = SECOND * 60;

	public static final long HOUR = MINUTE * 60;

	public static final long DAY = HOUR * 24;

	public static final long WEEK = DAY * 7;

	public static String dateToString(Date utilDate) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (utilDate == null) {
			return null;
		} else {
			return sdft.format(utilDate);
		}
	}

	public static String dateToStringHoure(Date utilDate) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHH");
		if (utilDate == null) {
			return null;
		} else {
			return sdft.format(utilDate);
		}
	}

	/**
	 * 取得系统当前时间
	 * 
	 * @return String yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date curDate = new java.util.Date();
		return sdf.format(curDate);
	}

	public static Date stringToDate(String datestr) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (datestr == null || datestr.trim().equals("")) {
				return null;
			} else {
				return sdft.parse(datestr);
			}
		} catch (Exception ex) {
			sdft = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				return sdft.parse(datestr);
			} catch (Exception exx) {

			}
			return null;
		}
	}
	
	
	public static Date stringToDates(String datestr) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			if (datestr == null || datestr.trim().equals("")) {
				return null;
			} else {
				return sdft.parse(datestr);
			}
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static String dateToStrings(java.util.Date curDate ) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdft.format(curDate);
	}

	/**
	 * 取得系统当前年月
	 * 
	 * @return String yyyy年mm月
	 * 
	 */
	public String getCurrentYearMonth() {
		SimpleDateFormat ymsdf = new SimpleDateFormat("yyyy年MM月");
		java.util.Date curDate = new java.util.Date();
		return ymsdf.format(curDate);
	}

	/**
	 * @function: 获取当前年
	 * @return String
	 * @date： 2008-8-1
	 * @Modifier: huangchen
	 * @ModDate: 2008-8-1
	 */
	public String getCurrentYear() {
		SimpleDateFormat yearsdf = new SimpleDateFormat("yyyy");
		java.util.Date curDate = new java.util.Date();
		return yearsdf.format(curDate);
	}

	/**
	 * 将字符串格式的日期'yyyy-MM-dd'转为'yyyy年MM月dd日'
	 * 
	 * @param date
	 *            字符串格式的日期'yyyy-MM-dd'
	 * @return String 类型的日期 'yyyy年MM月dd日'
	 * @throws ParseException
	 */
	public static String toOADate(String date) throws ParseException {
		SimpleDateFormat ymddf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null || date.trim().equals("")) {
			return "";
		} else {
			return ymddf.format(new java.util.Date(sdf.parse(date).getTime()));
		}
	}

	/**
	 * 将字符串格式的日期'yyyy-MM-dd hh:mm:ss'转为 java.sql.Date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static java.sql.Date toSqlDate(String date) throws ParseException {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date == null || date.trim().equals("")) {
			return null;
		} else {
			try {
				return new java.sql.Date(sdft.parse(date).getTime());
			} catch (ParseException e) {
				return new java.sql.Date(sdf.parse(date).getTime());
			}
		}
	}

	/**
	 * 取得系统当前时间
	 * 
	 * @return String yyyy-MM-dd hh:mm:ss
	 */
	public static String getCurrentDateHHMMSS() {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date curDate = new java.util.Date();
		return sdft.format(curDate);
	}

	public static Date getDateFromString(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (s == null || s.trim().equals("")) {
				return null;
			} else {
				return sdf.parse(s);
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static Date getYearMonFromString(String s) {
		SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");
		try {
			if (s == null || s.trim().equals("")) {
				return null;
			} else {
				return ym.parse(s);
			}
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 把日期型(util.Date)的数据转化为字符串型
	 * 
	 * @param utilDate
	 *            转化前的数据
	 * @return String 转化后的字符串
	 * 
	 * @author panhui
	 */
	public static String toString(java.util.Date utilDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (utilDate == null) {
			return null;
		} else {
			return sdf.format(utilDate);
		}
	}

	public static int compareTo(Date date1, Date date2) {
		return compareTo(date1, date2, false);
	}

	public static int compareTo(Date date1, Date date2, boolean ignoreMilliseconds) {

		// Workaround for bug in JDK 1.5.x. This bug is fixed in JDK 1.5.07. See
		// http://bugs.sun.com/bugdatabase/view_bug.do;:YfiG?bug_id=6207898 for
		// more information.

		if ((date1 != null) && (date2 == null)) {
			return -1;
		} else if ((date1 == null) && (date2 != null)) {
			return 1;
		} else if ((date1 == null) && (date2 == null)) {
			return 0;
		}

		long time1 = date1.getTime();
		long time2 = date2.getTime();

		if (ignoreMilliseconds) {
			time1 = time1 / SECOND;
			time2 = time2 / SECOND;
		}

		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}

	public static boolean equals(Date date1, Date date2) {
		if (compareTo(date1, date2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean equals(Date date1, Date date2, boolean ignoreMilliseconds) {

		if (!ignoreMilliseconds) {
			return equals(date1, date2);
		}

		long time1 = 0;

		if (date1 != null) {
			time1 = date1.getTime() / SECOND;
		}

		long time2 = 0;

		if (date2 != null) {
			time2 = date2.getTime() / SECOND;
		}

		if (time1 == time2) {
			return true;
		} else {
			return false;
		}
	}

	public static int getDaysBetween(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();

		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();

		endCal.setTime(endDate);

		int daysBetween = 0;

		while (beforeByDay(startCal.getTime(), endCal.getTime())) {
			startCal.add(Calendar.DAY_OF_MONTH, 1);

			daysBetween++;
		}

		return daysBetween;
	}

	public static boolean beforeByDay(Date date1, Date date2) {
		long millis1 = _getTimeInMillis(date1);
		long millis2 = _getTimeInMillis(date2);

		if (millis1 < millis2) {
			return true;
		} else {
			return false;
		}
	}

	private static long _getTimeInMillis(Date date) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int hour = 0;
		int minute = 0;
		int second = 0;

		cal.set(year, month, day, hour, minute, second);
		long millis = cal.getTimeInMillis() / DAY;
		return millis;
	}

	public static boolean isVariableDate(Date date) {
		if (date == null) {
			return false;
		}
		long fromtime = new Date().getTime() - 90 * DAY;
		long endtime = new Date().getTime() + 5 * DAY;
		if (date.getTime() > fromtime && date.getTime() < endtime) {
			return true;
		}
		return false;

	}
}
