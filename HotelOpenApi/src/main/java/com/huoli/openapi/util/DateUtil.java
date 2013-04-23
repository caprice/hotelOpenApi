/**
 * 项目名称 : newDervie
 * 创建用户 : lanlei
 * 文档名称 : DateHelper.java
 * 文档位置 : wh.mj.util
 * 最后变更 : lanlei
 * 变更日期 : Aug 15, 2009
 * 当前版本 : Revision: 1 
 * 
 * Copyright (c) 2007 Wh-Mj Engineering Corp, Inc. All Rights Reserved.
 *
 */
package com.huoli.openapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 作用说明：日期处理类 生成日期：Aug 15, 2009
 * 
 */
public class DateUtil {

	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static String YYYY_MM_DD = "yyyy-MM-dd";
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static String YYYYMMDD = "yyyyMMdd";
	public static String MMDD = "MM/dd";

	/**
	 * 把Date转换为缺省的日期格式字串，缺省的字转换格式为yyyy-MM-dd HH:mm:ss 如：2004-10-10 20:12:10
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串转换为时间，格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDate(String dateStr) {
		Date date = null;
		SimpleDateFormat dateFormat = null;
		if (dateStr.length() > 10) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}

		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Return the current date string
	 * 
	 * @return － 产生的日期时间字符串<br>
	 */
	public static String getCurrentDateTimeStr() {
		Calendar cal = Calendar.getInstance();
		Date currDate = cal.getTime();
		return formatDate(currDate, C_TIME_PATTON_DEFAULT);
	}
	
	/**
     * 返回给定天的下一天的字符串
     * 
     * @param curDay
     * @return
     */
    public static String getNextDay(String curDay) {
        SimpleDateFormat sdf = null;
        Calendar cal = null;
        Date date = null;
        String dateStr = null;

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            date = sdf.parse(curDay);
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date = cal.getTime();
            dateStr = sdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.println("错误的日期格式！");
            // e.printStackTrace();
            return curDay;
        }

        return dateStr;
    }
	

	/**
	 * 返回当前的格式化字符串
	 * 
	 * @return
	 */
	public static String getCurDay() {
		SimpleDateFormat sdf = null;
		Calendar cal = null;
		String dateStr = null;

		sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);

		cal = Calendar.getInstance();
		dateStr = sdf.format(cal.getTime());

		return dateStr;
	}

	/**
	 * 比较两个字符串所表示的天的大小
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int compareDate(String a, String b) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date _a = sdf.parse(a);
			Date _b = sdf.parse(b);
			if (_a.after(_b)) {
				return 1;
			} else if (_a.before(_b)) {
				return -1;
			} else {
				return 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 判断时间是否再今天之前
	 * 
	 * @param time
	 * @param f
	 * @return
	 * @throws ParseException
	 */
	public static boolean isBeforeToday(String time, String f)
			throws ParseException {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat(f);
		Date d = format.parse(time);
		return d.before(now);
	}
    /**
     * 取得指定日期N天后的日期
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }
	/**
	 * 获取两个日期相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDifferenceDays(Date date1, Date date2) {
		String d1 = DateUtil.formatDate(date1, YYYY_MM_DD);
		String d2 = DateUtil.formatDate(date2, YYYY_MM_DD);
		return getDifferenceDays(d1, d2);

	}

	/**
	 * 获取两个日期相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDifferenceDays(String date1, String date2) {
		Date d1 = DateUtil.toDate(date1, YYYY_MM_DD);
		Date d2 = DateUtil.toDate(date2, YYYY_MM_DD);
		return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static void main(String[] args) {
		System.out.println(-1 / 10);
		System.out.println(DateUtil.getDifferenceDays("2013-04-26", "2013-04-01"));
	}

	/**
	 * 返回当前日期
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String date = sdf.format(cal.getTime());
		return date;
	}

	/**
	 * 字符串转换为时间格式
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date toDate(String dateStr, String pattern) {
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 根据特定的Pattern,把Date转换为相应的日期格式字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	/**
	 * 根据特定的Pattern,把Date转换为相应的日期格式字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 时间类转换成对应日期类
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar DateToCanlenDer(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * String 转化Calendar
	 * 
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Calendar convertStringToCalendar(String str, String format)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(str);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * 获得特定日期的星期字串
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		week = week - 1;
		String weekStr = "";
		switch (week) {
		case 0:
			weekStr = "星期日";
			break;
		case 1:
			weekStr = "星期一";
			break;
		case 2:
			weekStr = "星期二";
			break;
		case 3:
			weekStr = "星期三";
			break;
		case 4:
			weekStr = "星期四";
			break;
		case 5:
			weekStr = "星期五";
			break;
		case 6:
			weekStr = "星期六";
		}
		return weekStr;
	}

	/**
	 * 取两个日期对象毫秒差数,用时间1-时间2
	 * 
	 * @param date1
	 *            日期对象
	 * @param date2
	 *            日期对象
	 * @return 毫秒差数
	 */
	public static long getDateMiliDispersion(Date date1, Date date2) {
		/* 如果其中一为空,则返0 */
		if (date1 == null || date2 == null)
			return 0;

		/* 提取毫秒 */
		long time1 = date1.getTime();
		long time2 = date2.getTime();

		return time1 - time2;
	}

	/**
	 * 比较两个时间的差，返回天数，用时间1-时间2
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 两个时间相差的天数
	 */
	public static int getDateDiff(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		long time1 = date1.getTime();
		long time2 = date2.getTime();

		long diff = time1 - time2;

		Long longValue = new Long(diff / (60 * 1000 * 60 * 24));
		return longValue.intValue();
	}

	/**
	 * 比较两个时间的差，返回分钟数，用时间1-时间2 注解： 作者：lanlei 生成日期：Aug 15, 2009
	 * 
	 * @param date1
	 *            时间1
	 * @param date2
	 *            时间2
	 * @return 两个时间相差的分钟数
	 */
	public static int getDateMinute(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		long time1 = date1.getTime();
		long time2 = date2.getTime();

		long diff = time1 - time2;

		Long longValue = new Long(diff / (60 * 1000));

		int di = longValue.intValue();
		;
		return di;
	}

	/**
	 * 
	 * 描述：获得当前时间 创建日期：2010-6-18下午02:19:51
	 * 
	 * @return
	 */
	public static String getCurrentDate(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date()).toString();
	}

	/**
	 * 提取当前时间的星期数，7是星期天，1是星期一，如此类推
	 * 
	 * @return 序数
	 */
	public static int getCurrentWeek() {
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		week = week - 1;
		if (week == 0) {
			week = 7;
		}
		return week;
	}

	/**
	 * 提取当前的星期数的中文字串，星期一...星期日
	 * 
	 * @return
	 */
	public static String getCurrentWeekStr() {
		return getWeekStr(new Date());
	}

	/**
	 * 提取当前时间的年份
	 * 
	 * @return 年份
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 提取当前时间的月份,1:一月份 12：12月份
	 * 
	 * @return 月份
	 */
	public static int getCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 提取当前时间的日期
	 * 
	 * @return 日期
	 */
	public static int getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 根据当前时间，增减时间
	 */
	public static Date addTime(int fieid, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(fieid, amount);
		return calendar.getTime();
	}

	/**
	 * 根据传入时间，增减时间
	 */
	public static Date addTime(Date datetime, int fieid, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datetime);
		calendar.add(fieid, amount);
		return calendar.getTime();
	}

	/**
	 * 在日期上增加分钟
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMinute(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加小时
	 * 
	 * @param date
	 *            日期
	 * @param n
	 *            小时数
	 * @return
	 */
	public static Date addHour(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数个整日
	 * 
	 * @param date
	 *            日期
	 * @param n
	 *            天数
	 * @return
	 */
	public static Date addDay(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);
		return cal.getTime();
	}

	/**
	 * 在日期上增加数个整月
	 * 
	 * @param date
	 *            日期
	 * @param n
	 *            月数
	 * @return
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	/**
	 * 将格式为2006-12-29 16:30的字符串转换成 20061229163000 字符格式
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatDateTime(String dateTime) {
		if (dateTime != null && dateTime.length() >= 8) {
			String formatDateTime = dateTime.replaceAll("-", "");
			formatDateTime = formatDateTime.replaceAll(":", "");
			String date = formatDateTime.substring(0, 8);
			String time = formatDateTime.substring(8).trim();// 从第8位开始截取，直到遇到空格为止
			for (int i = time.length(); i < 6; i++)// 后6位不足的补充0
			{
				time += "0";
			}
			return date + time;
		} else {
			return "";
		}
	}

	/**
	 * 将格式为2006-12-29 16:30的日期转换成 20061229163000字符格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		String dateTime = formatDate(date);
		return formatDateTime(dateTime);
	}

	/**
	 * 获取day天以前的时间
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String getDateBefore(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return formatDate(now.getTime());
	}

	/**
	 * 根据传入的开始月日和结束月日判断当前时间是否在此时间区间内
	 * 
	 * @param startMonth
	 * @param startDay
	 * @param endMonth
	 * @param endDay
	 * @return
	 */
	public static boolean isInTimeZone(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(startTime);
			endDate = sdf.parse(endTime);
		} catch (Exception e) {
			logger.error("isInTimeZone:", e);
		}

		long start = startDate.getTime();
		long end = endDate.getTime();
		long l = new Date().getTime();

		logger.info("start is :" + start);
		logger.info("end is :" + end);
		logger.info("l is :" + l);

		if (l > start && l <= end) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 比较时间
	 * 
	 * @param startDate
	 * @return
	 */
	public static boolean getDiffer(Date start, Date end) {
		boolean days = false;
		if (start.getTime() > end.getTime()) {
			days = true;
		}
		return days;
	}
}
