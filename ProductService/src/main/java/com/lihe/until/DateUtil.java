package com.lihe.until;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	private static Logger L = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * yyyy-MM-dd
	 */
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	private static Calendar gregorianCalendar = null;
	
	/**
	 * 计算出2个日期相差的天数
	 * @param d1
	 *            日期1
	 * @param d2
	 *            日期2
	 * @return 日期差数
	 */
	public static int countDiffDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.MILLISECOND, 0); 
		c2.setTime(d2);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.MILLISECOND, 0);
		int returnInt = 0;
		if (c1.before(c2)) { //c1日期小于c2
			while (c1.before(c2)) {
				c1.add(Calendar.DAY_OF_MONTH, 1);
				returnInt++;
			}
		} else {
			while (c2.before(c1)) {
				c2.add(Calendar.DAY_OF_MONTH, 1);
				returnInt++;
			}
		}
		return (returnInt);
	}

	/**
	 * longtime转换为时间
	 * @param longTime
	 * @return
	 */
	public static Date getDateByLongTime(Long longTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(longTime);
		return cal.getTime();
	}

	/**
	 * 时间转换字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获取偏移时间值
	 * @param date
	 * @param field
	 * @param offData
	 * @return
	 */
	public static Date getOffDataDate(Date date, Integer field,  Integer offData) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, offData);
		return cal.getTime();
	}

	/**
	 * 字符串转换时间
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String dateStr, String format) {
		if (StringQedUtil.isBlank(dateStr)) {
			return null;
		}
		if (StringQedUtil.isBlank(format)) {
			format = FORMAT_DATE;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(dateStr);
		} catch (Exception e) {
			L.error("method getDate error : ", e);
			return null;
		}
	}

    /**
     * 获取0点0分0秒的日期值
     *
     * @param d 传入日期
     * @return Date 传出日期
     */
    public static Date getDateWithZeroColck(Date d) {
        if (null == d) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


	/**
	 248      * 获取日期前一天
	 249      *
	 250      * @param date
	 251      * @return
	 252      */
    public static Date getDayBefore(Date date) {
         gregorianCalendar.setTime(date);
         int day = gregorianCalendar.get(Calendar.DATE);
         gregorianCalendar.set(Calendar.DATE, day - 1);
         return gregorianCalendar.getTime();
     }


	public static int getCountDayBeforeMonth(Date date,int count) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -count);
		return countDiffDay(cal.getTime(), date);
	}
}
