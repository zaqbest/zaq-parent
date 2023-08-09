/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.web.utils;

import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ***************************************************************************
 * 创建时间 : 2016年7月28日
 * 实现功能 : 日期操作辅助类
 * 作者 : yanping.shi
 * 版本 : v0.0.1
 * -----------------------------------------------------------------------------
 * 修改记录:
 * 日 期 版本 修改人 修改内容
 * 2016年7月28日 v0.0.1 yanping.shi 创建
 * ***************************************************************************
 */
public final class DateUtil {
    private DateUtil() {
    }

    // 日期格式
    public static String PATTERN_DATE = "yyyy-MM-dd";

    public static String PATTERN_SHORT_DATE = "yyyyMMdd";

    public static String PATTERN_SMALL_DATE = "yyMMdd";

    // 日期时间格式
    public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static String PATTERN_SHORT_DATETIME = "yyyyMMddHHmmss";

    // 时间格式
    public static String PATTERN_TIME = "HH:mm:ss";

    public static String PATTERN_SHORT_TIME = "HHmmss";

    public static int TYPE_DAY = Calendar.DAY_OF_MONTH;// 按天

    public static int TYPE_MONTH = Calendar.MONTH;

    public static int TYPE_YEAR = Calendar.YEAR;

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date) {
        return format(date, PATTERN_DATE);
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @return
     */
    public static final String formatDateTime(Object date) {
        return format(date, PATTERN_DATETIME);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static final String getDate() {
        return format(new Date());
    }

    /**
     * 获取短日期
     *
     * @return
     */
    public static final String getShortDate() {
        return format(new Date(), PATTERN_SHORT_DATE);
    }

    /**
     * ====================================================================
     * 功 能： 获得短短日期
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年6月13日 v0.0.1 yanping.shi 创建
     * ====================================================================
     */
    public static final String getSmallDate() {
        return format(new Date(), PATTERN_SMALL_DATE);
    }

    /**
     * 获取短时间
     */
    public static final String getShortTime() {
        return format(new Date(), PATTERN_SHORT_TIME);
    }

    /**
     * 获取日期时间
     *
     * @return
     */
    public static final String getDateTime() {
        return format(new Date(), PATTERN_DATETIME);
    }

    /**
     * 获取日期
     *
     * @param pattern
     * @return
     */
    public static final String getDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期计算
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static final Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static final Date genarateDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    } 

    /**
     * 字符串转换为日期:不支持yyM[M]d[d]格式
     *
     * @param date
     * @return
     */
    public static final Date stringToDate(String date) {
        if (StrUtil.isBlank(date)) {
            return null;
        }
        String separator = String.valueOf(date.charAt(4));
        String pattern = "yyyyMMdd";
        if (!separator.matches("\\d*")) {
            pattern = "yyyy" + separator + "MM" + separator + "dd";
            if (date.length() < 10) {
                pattern = "yyyy" + separator + "M" + separator + "d";
            }
        } else if (date.length() < 8) {
            pattern = "yyyyMd";
        }
        pattern += " HH:mm:ss.SSS";
        pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getDayBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        long n = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (n / (60 * 60 * 24 * 1000L));
    }

    /**
     * 间隔月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        return n;
    }

    /**
     * 间隔月，多一天就多算一个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1 = start.get(Calendar.YEAR);
        int year2 = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n = (year2 - year1) * 12;
        n = n + month2 - month1;
        int day1 = start.get(Calendar.DAY_OF_MONTH);
        int day2 = end.get(Calendar.DAY_OF_MONTH);
        if (day1 <= day2) {
            n++;
        }
        return n;
    }

    /**
     * ====================================================================
     * 功 能： 按中文日期格式化日期显示
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年7月24日 v0.0.1 liang.lei 创建
     * ====================================================================
     */
    public static final String formatDateForChinese(Date date) {
        Calendar formatDate = Calendar.getInstance();
        formatDate.setTime(date);
        String year = String.valueOf(formatDate.get(Calendar.YEAR));
        int month = formatDate.get(Calendar.MONTH) + 1;
        String monthstr = month < 10 ? "0" + month : "" + month;
        int day = formatDate.get(Calendar.DAY_OF_MONTH);
        String daystr = day < 10 ? "0" + day : "" + day;
        return year + " 年 " + monthstr + " 月 " + daystr + " 日";
    }

    /**
     * ====================================================================
     * 功 能： UTC时间转本地时间
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年8月14日 v0.0.1 liang 创建
     * ====================================================================
     */
    public static final Date formatToUTCDate(Date localDateTime) {
        // 1、取得本地时间：
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(localDateTime);

        // 2、取得时间偏移量：
        int zoneOffset = localCalendar.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = localCalendar.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        localCalendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return localCalendar.getTime();
    }

    /**
     * ====================================================================
     * 功 能： UTC时间转本地时间
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2017年8月14日 v0.0.1 liang 创建
     * ====================================================================
     */
    public static final Date formatToLocalDate(Date utcDateTime) {
        // 1、取得本地时间：
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(utcDateTime);

        // 2、取得时间偏移量：
        int zoneOffset = localCalendar.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = localCalendar.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        localCalendar.add(Calendar.MILLISECOND, (zoneOffset + dstOffset));
        return localCalendar.getTime();
    }
    
    
    /**
     * ====================================================================
     *功 能： ： 判断日期是否当前月的第一天
    ----------------------------------------------------------------------
     *修改记录 ：
     *日 期  版本 修改人 修改内容
     *2020年2月17日 v1.0.0 micha 创建
    ====================================================================
     */
    public static boolean isFirstDayOfMonth(Date date) {
    	if (date == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
    }
    /**
     * ====================================================================
     *功 能： 判断日期是否当前月的最后一天
    ----------------------------------------------------------------------
     *修改记录 ：
     *日 期  版本 修改人 修改内容
     *2020年2月17日 v1.0.0 micha 创建
    ====================================================================
     */
	public static boolean isLastDayOfMonth(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * ====================================================================
	 *功 能： 判断日期是否当前月的倒数第二天
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2020年10月13日 v0.0.1 micha 创建
	====================================================================
	 */
	public static boolean isLastNextDayOfMonth(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 2));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}
	/**
	 *  获取某个日期的开始时间
	 * @param d
	 * @return
	 */
	public static java.sql.Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new java.sql.Timestamp(calendar.getTimeInMillis());
	}

	/**
	 *  获取某个日期的结束时间
	 * @param d
	 * @return
	 */
	public static java.sql.Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new java.sql.Timestamp(calendar.getTimeInMillis());
	}
	
	/**
	 *  获取当天的开始时间
	 * @param d
	 * @return
	 */
	public static Date getDayBegin(Date d) {
		return getDayStartTime(d);
	}

	/**
	 *  获取当天的结束时间
	 * @param d
	 * @return
	 */
	public static Date getDayEnd(Date d) {
		return getDayEndTime(d);
	}
	
	/**
	 * 获取本周的开始时间
	 * @param d
	 * @return
	 */
	public static Date getBeginDayOfWeek(Date d) {
		if (d == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	/**
	 *  获取本周的结束时间
	 * @param d
	 * @return
	 */
	public static Date getEndDayOfWeek(Date d) {
		if (d == null) {
			return null;
		}
		//获取本周的开始时间
		Date startDtDate=getBeginDayOfWeek(d);
		
		//本周开始日期+6天
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDtDate);
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	/**
	 *  获取本月的开始时间
	 * @param d
	 * @return
	 */
	public static Date getBeginDayOfMonth(Date d) {
		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(getYear(d), getMonth(d) - 1, 1);
		return getDayStartTime(calendar.getTime());
	}

	/**
	 *  获取本月的结束时间
	 * @param d
	 * @return
	 */
	public static Date getEndDayOfMonth(Date d) {
		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(getYear(d), getMonth(d) - 1, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getYear(d), getMonth(d) - 1, day);
		return getDayEndTime(calendar.getTime());
	}

	/**
	 *  获取本年的开始时间
	 * @param d
	 * @return
	 */
	public static Date getBeginDayOfYear(Date d) {
		if (d == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear(d));
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return getDayStartTime(cal.getTime());
	}

	/**
	 *  获取本年的结束时间
	 * @param d
	 * @return
	 */
	public static Date getEndDayOfYear(Date d) {
		if (d == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear(d));
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		return getDayEndTime(cal.getTime());
	}

	/**
	 *  获取今年是哪一年
	 * @param d
	 * @return
	 */
	public static int getYear(Date d) {
		if (d == null) {
			return 0;
		}
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		return Integer.valueOf(gc.get(1));
	}

	/**
	 *  获取本月是哪一月
	 * @param d
	 * @return
	 */
	public static int getMonth(Date d) {
		if (d == null) {
			return 0;
		}
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		return gc.get(2) + 1;
	}
	
	/**
	 * ====================================================================
	 *功 能： 日期月份增减
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2020年12月25日 v1.0.0 fengzhiwen 创建
	====================================================================
	 */
	public static Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, month);
        return c.getTime();
	}
	
}
