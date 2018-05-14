package com.qdone.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 时间处理
 * @author傅为地 
 * 
 */
public class DateUtil extends org.apache.commons.lang.time.DateUtils{

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd
	 */
	public static synchronized String getStringDateShort() {
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取转换后日期字符串
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized String getStringDateByDate(Date date) {
		String dateString = "";
		if (date != null) {
			dateString=DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return dateString;
	}

	/**
	 * 获得距离当天指定天数的日期
	 * 
	 * @param day
	 *            (间隔天数(负数提前,正数滞后))
	 * @return [参数说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static synchronized Date getDateFromToday(int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) + day);
		return c.getTime();
	}

	/**
	 * 获取年月日
	 */
	public static synchronized  String getDateByCalendar(Calendar cal) {
		return DateFormatUtils.format(cal.getTime(),"yyyy-MM-dd");
	}

	/**
	 * 获取开始 、结束日期 flag : 0 当天； 1 近一周；2 当月 ； 3 近一月
	 */

	public static synchronized Map<String, Object> getDateMap(String flag) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance();
		// 判断
		if ("0".equals(flag)) {
			ca.setTime(new Date());
			paraMap.put("endDate", ca.getTime());
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("startDateStr", getDateByCalendar(ca));
		} else if ("1".equals(flag)) {
			ca.setTime(new Date());
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.add(Calendar.DAY_OF_YEAR, -6);
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("2".equals(flag)) {
			ca.setTime(new Date());
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.set(Calendar.DAY_OF_MONTH, 1);
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("3".equals(flag)) {
			ca.setTime(new Date());
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.add(Calendar.MONTH, -1);
			ca.set(Calendar.HOUR_OF_DAY, 24);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		}
		return paraMap;
	}

	/**
	 * 获取当月时间参数
	 * 
	 * @param flag
	 *            true:月初时间 false:月末时间
	 * @return
	 */
	public static synchronized Date getNowMonthDate(boolean flag) {
		Calendar rightNow = Calendar.getInstance();
		if (flag) {
			// 当前月的第一天
			rightNow.set(GregorianCalendar.DAY_OF_MONTH, 1);
			rightNow.set(Calendar.HOUR_OF_DAY, 0);
			rightNow.set(Calendar.MINUTE, 0);
			rightNow.set(Calendar.SECOND, 0);
		} else {
			// 当前月的最后一天
			rightNow.set(Calendar.DATE, 1);
			rightNow.roll(Calendar.DATE, -1);
			rightNow.set(Calendar.HOUR_OF_DAY, 23);
			rightNow.set(Calendar.MINUTE, 59);
			rightNow.set(Calendar.SECOND, 59);
		}
		return rightNow.getTime();
	}

	/**
	 * 获取某时刻所在月的 开始/截止时刻
	 * 
	 * @param date:统计时刻
	 * @param flag：统计标识
	 * @return
	 */
	public static synchronized Date getMonthDate(Date date, boolean flag) {
		Calendar rightMonth = Calendar.getInstance();
		rightMonth.setTime(date);
		if (flag) {
			// 当前月的第一天
			rightMonth.set(GregorianCalendar.DAY_OF_MONTH, 1);
			rightMonth.set(Calendar.HOUR_OF_DAY, 0);
			rightMonth.set(Calendar.MINUTE, 0);
			rightMonth.set(Calendar.SECOND, 0);
		} else {
			// 当前月的最后一天
			rightMonth.set(Calendar.DATE, 1);
			rightMonth.roll(Calendar.DATE, -1);
			rightMonth.set(Calendar.HOUR_OF_DAY, 23);
			rightMonth.set(Calendar.MINUTE, 59);
			rightMonth.set(Calendar.SECOND, 59);
		}
		return rightMonth.getTime();
	}

	/**
	 * 得到当前时间
	 * 
	 * @return
	 */
	public static synchronized Date getNowDate() {
		Calendar cl = Calendar.getInstance();
		return cl.getTime();
	}

	/**
	 * 格式化时间
	 * 
	 * @return
	 */
	public static synchronized String getFullTime() {
		Calendar cl = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(cl.getTime());
	}

	/**
	 * 格式化时间
	 * 
	 * @return
	 */
	public static synchronized String getDateFormat(Date date, String parent) {
		return DateFormatUtils.format(date,parent);
	}

	/**
	 * 计算两个日期相差的月份数
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @param pattern
	 *            日期1和日期2的日期格式
	 * @return 相差的月份数
	 * @throws ParseException
	 */
	public static synchronized int countBetweenMonths(String date1, String date2, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));

		int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

		// 开始日期若小月结束日期
		if (year < 0) {
			year = -year;
			return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		}

		return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	}

	/**
	 * 获取当月时间参数
	 * 
	 * @param flag
	 *            true:月初时间 false:月末时间
	 * @param dateString:日期字符串
	 * @param pattern:日期格式
	 * @return
	 * @throws ParseException
	 */
	public static synchronized Date getInputMonthDate(boolean flag, String dateString, String pattern) throws ParseException {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new SimpleDateFormat(pattern).parse(dateString));
		if (flag) {
			// 当前月的第一天
			rightNow.set(GregorianCalendar.DAY_OF_MONTH, 1);
			rightNow.set(Calendar.HOUR_OF_DAY, 0);
			rightNow.set(Calendar.MINUTE, 0);
			rightNow.set(Calendar.SECOND, 0);
		} else {
			// 当前月的最后一天
			rightNow.set(Calendar.DATE, 1);
			rightNow.roll(Calendar.DATE, -1);
			rightNow.set(Calendar.HOUR_OF_DAY, 23);
			rightNow.set(Calendar.MINUTE, 59);
			rightNow.set(Calendar.SECOND, 59);
		}
		return rightNow.getTime();
	}

	/**
	 * 获取开始 、结束日期 inputDate:输入一个时刻，日期 flag : 0 当天； 1 近一周；2 当月 ； 3 近一月
	 * direction:0 向前 1 向后 比如：某个时刻，向后退一个月 flag:0 direction:1
	 */

	public static synchronized Map<String, Object> getDateRangeMap(Date inputDate, String flag, String direction) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance();
		ca.setTime(inputDate);
		// 判断
		if ("0".equals(flag)) {
			paraMap.put("endDate", ca.getTime());
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("startDateStr", getDateByCalendar(ca));
		} else if ("1".equals(flag) & "0".equals(direction)) {
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.add(Calendar.DAY_OF_YEAR, -6);// 提前一个周
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("1".equals(flag) & "1".equals(direction)) {
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
			ca.add(Calendar.DAY_OF_YEAR, 6);// 滞后一个周
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
		} else if ("2".equals(flag)) {
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.set(Calendar.DAY_OF_MONTH, 1);//
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("3".equals(flag) & "0".equals(direction)) {
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.add(Calendar.MONTH, -1);
			ca.set(Calendar.HOUR_OF_DAY, 24);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("3".equals(flag) & "1".equals(direction)) {
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
			ca.add(Calendar.MONTH, 1);
			ca.set(Calendar.HOUR_OF_DAY, 24);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
		}
		return paraMap;
	}

	/**
	 * 输入的某个时刻inputDate，向前或者向后 推(index)个月 direction:0 向前 1 向后 向前或向后推:index 个月
	 */
	public static synchronized Map<String, Object> getRangeMonthMap(Date inputDate, int index, String direction) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		Calendar ca = Calendar.getInstance();
		ca.setTime(inputDate);
		if ("0".equals(direction)) {
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
			ca.add(Calendar.MONTH, (0 - index));
			ca.set(Calendar.HOUR_OF_DAY, 24);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
		} else if ("1".equals(direction)) {
			paraMap.put("stratDate", ca.getTime());
			paraMap.put("stratDateStr", getDateByCalendar(ca));
			ca.add(Calendar.MONTH, (0 + index));
			ca.set(Calendar.HOUR_OF_DAY, 24);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			paraMap.put("endDate", ca.getTime());
			paraMap.put("endDateStr", getDateByCalendar(ca));
		}
		return paraMap;
	}

	/**
	 * 计算执行时刻，所在具体间隔区间段 时间跨度单位是月
	 * 
	 * @param start:规则起始时刻
	 * @param end:重置执行时刻
	 * @param range循环时间步长
	 * @return
	 */
	public static synchronized int getRangeStepMonth(Date start, Date end, int range) {
		int step = 0;// 间隔数目
		while (true) {
			if (end.compareTo(getDateRangeMonth(start, step * range)) == -1
					|| end.compareTo(getDateRangeMonth(start, (step + 1) * range)) == 1) {// 不符合
				step++;
				continue;
			} else {// 在某个间隔之内
				break;
			}
		}
		return step;
	}

	/**
	 * 计算执行时刻，所在具体间隔区间段 时间跨度是天
	 * 
	 * @param start:规则起始时刻
	 * @param end:充值执行时刻
	 * @param range循环时间步长
	 * @return
	 */
	public static synchronized int getRangeStepDay(Date start, Date end, int range) {
		int step = 0;// 间隔数目
		while (true) {
			if (end.compareTo(getDateRangeDay(start, step * range)) == -1
					|| end.compareTo(getDateRangeDay(start, (step + 1) * range)) == 1) {// 不符合
				step++;
				continue;
			} else {// 在某个间隔之内
				break;
			}
		}
		return step;
	}

	/**
	 * 计算开始时刻，到截止时刻之间 经过了多少周期跨度
	 * 
	 * @param start:开始时刻
	 * @param end:截止时刻
	 * @param range:单位天
	 * @param type:
	 *            05 年，04季度, 03月, 02周, 01天
	 * @return
	 */
	public static synchronized int getRangeStep(Date start, Date end, String type, int range) {
		Double daySecond = 24 * 60 * 60 * 1000.00;// 毫秒数/天
		Double max = 0d, min = 0d;
		if (type.equalsIgnoreCase("05")) {// 年
			max = (end.getTime() - start.getTime()) / (daySecond * 365 * range);
			min = (end.getTime() - start.getTime()) / (daySecond * 366 * range);
		}
		if (type.equalsIgnoreCase("04")) {// 季度
			max = (end.getTime() - start.getTime()) / (daySecond * 89 * range);
			min = (end.getTime() - start.getTime()) / (daySecond * 92 * range);
		}
		if (type.equalsIgnoreCase("03")) {// 月
			max = (end.getTime() - start.getTime()) / (daySecond * 28 * range);
			min = (end.getTime() - start.getTime()) / (daySecond * 31 * range);
		}
		if (type.equalsIgnoreCase("02") || type.equalsIgnoreCase("01")) {// 周，天
			max = (end.getTime() - start.getTime()) / (daySecond * range);
			min = (end.getTime() - start.getTime()) / (daySecond * range);
		}
		return (int) (Math.abs(max - min) < 1 ? Math.floor(max) : Math.floor(min));
	}

	/**
	 * 获得距离某个时刻，指定天数间隔的时间结点
	 * 
	 * @param date:输入的起始时刻
	 * @param day:间隔天数(负数提前,正数滞后)
	 * @return Date 截止时刻
	 */
	public static synchronized Date getDateRangeDay(Date date, int day) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DATE, ca.get(Calendar.DATE) + day);
		return ca.getTime();
	}

	/**
	 * 获得距离某个时刻，指定月数间隔的时间结点
	 * 
	 * @param date:输入的起始时刻
	 * @param day:间隔月数(负数提前,正数滞后)
	 * @return Date 截止时刻
	 */
	public static synchronized Date getDateRangeMonth(Date date, int range) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, range);
		return ca.getTime();
	}

	/**
	 * 判断当前时刻是星期几 返回所在自然周的起始时刻，截止时刻 Date[0]:自然周的起始时刻 Date[1]:自然周的截止时刻
	 */
	public static synchronized Date[] getDateWeekInfo(Date date) {
		Date[] result = new Date[2];
		String flag = new SimpleDateFormat("E").format(date);
		if (flag.equalsIgnoreCase("星期一")) {
			result[0] = getDayInfo(date, true);
			result[1] = getDayInfo(getDateRangeDay(date, 7), true);
		} else if (flag.equalsIgnoreCase("星期二")) {
			result[0] = getDayInfo(getDateRangeDay(date, -1), true);
			result[1] = getDayInfo(getDateRangeDay(date, 6), true);
		} else if (flag.equalsIgnoreCase("星期三")) {
			result[0] = getDayInfo(getDateRangeDay(date, -2), true);
			result[1] = getDayInfo(getDateRangeDay(date, 5), true);
		} else if (flag.equalsIgnoreCase("星期四")) {
			result[0] = getDayInfo(getDateRangeDay(date, -3), true);
			result[1] = getDayInfo(getDateRangeDay(date, 4), true);
		} else if (flag.equalsIgnoreCase("星期五")) {
			result[0] = getDayInfo(getDateRangeDay(date, -4), true);
			result[1] = getDayInfo(getDateRangeDay(date, 3), true);
		} else if (flag.equalsIgnoreCase("星期六")) {
			result[0] = getDayInfo(getDateRangeDay(date, -5), true);
			result[1] = getDayInfo(getDateRangeDay(date, 2), true);
		} else {
			result[0] = getDayInfo(getDateRangeDay(date, -6), true);
			result[1] = getDayInfo(getDateRangeDay(date, 1), true);
		}
		return result;
	}

	/**
	 * 获取某个时刻所在年 起始时刻和截止时刻
	 * 
	 * @param date
	 * @return
	 */
	public static synchronized Date[] getDateYearInfo(Date date) {
		Date[] result = new Date[2];
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		String start = (ca.get(Calendar.YEAR) + 1900) + "-1-1 00:00:00";
		String end = (ca.get(Calendar.YEAR) + 1900) + "-12-31 23:59:59";
		result[0] = parseDate(start);
		result[1] = parseDate(end);
		return result;
	}

	/**
	 * 获得某一时刻的当天的起始时刻，截止时刻
	 * 
	 * @param date
	 * @param flag
	 * @return
	 */
	public static synchronized Date getDayInfo(Date date, boolean flag) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if (flag) {// 当前起始时刻
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
		} else {// 截止时刻
			ca.set(Calendar.HOUR_OF_DAY, 23);
			ca.set(Calendar.MINUTE, 59);
			ca.set(Calendar.SECOND, 59);
		}
		return ca.getTime();
	}

	/**
	 * 依指定格式将一个字符串转化为java.util.Date类型
	 * 
	 * @param src
	 *            要转换的字符串
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static synchronized Date parseDate(String src, String format) {
		Date date = null;
		if (src == null || src.equals(""))
			return null;
		try {
			if (format == null || format.length() == 0 || format.equals("")) {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(src);
			} else {
				date = new SimpleDateFormat(format).parse(src);
			}
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 依指定格式将一个字符串转化为java.util.Date类型
	 * 
	 * @param src
	 *            要转换的字符串
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static synchronized Date parseDate(String src) {
		Date date = null;
		if (src == null || src.equals(""))
			return null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(src);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 当前时间格式化
	 * 
	 * @return
	 */
	public static synchronized String getCurDateString() {
		return getDateString(new Date(), "yyyy-MM-dd HH:mm:ss SSS");
	}

	/**
	 * 当前时间特定格式化
	 * 
	 * @param formatString
	 * @return
	 */
	public static synchronized String getCurrentDateString(String formatString) {
		Date currentDate = new Date();
		return getDateString(currentDate, formatString);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param formatString
	 * @param locale
	 * @return
	 */
	public static synchronized String getDateString(Date date, String formatString) {
		return getDateString(date, formatString, Locale.PRC);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param formatString
	 * @param locale
	 * @return
	 */
	public static synchronized String getDateString(Date date, String formatString, Locale locale) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatString, locale);
			return dateFormat.format(date);
		}
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static synchronized String formatDateTime(Date date) {
		return date==null?"":DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到日期时间字符串，转换指定格式
	 */
	public static synchronized String formatDateTime(Date date, String pattern) {
		pattern=StringUtils.isEmpty(pattern)?"yyyy-MM-dd HH:mm:ss":pattern;
		return date==null?"":DateFormatUtils.format(date, pattern);
	}
}
