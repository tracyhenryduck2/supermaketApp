/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.guide.ex.ecdemo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间转换工具类
 * @author Jorstin Chan@容联•云通讯
 * @date 2014-12-10
 * @version 4.0
 */
public class DateUtil {
	
	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
	private static final long ONEDAY = 86400000;
	public static final int SHOW_TYPE_SIMPLE = 0;
	public static final int SHOW_TYPE_COMPLEX = 1;
	public static final int SHOW_TYPE_ALL = 2;
	public static final int SHOW_TYPE_CALL_LOG = 3;
	public static final int SHOW_TYPE_CALL_DETAIL = 4;

	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 *
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @return
	 * @author Administrator
	 * @category 获取下面一个礼拜的日期
	 */
	public static List<Map<String,Object>> getNextWeekDateTable(){
		ArrayList<Map<String,Object>> timetable = new ArrayList<Map<String,Object>>();
		long timenow = getCurrentDayTime();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(timenow));
		
		for(int i=0;i<7;i++){
			Map<String,Object> map =new HashMap<String, Object>();
			String dayofweek = "";
			String status="";
			int ds =c.get(Calendar.DAY_OF_WEEK);
			switch(ds){
			case 1:
				dayofweek = "周日";
				break;
			case 2:
				dayofweek = "周一";
				break;
			case 3:
				dayofweek = "周二";
				break;
			case 4:
				dayofweek = "周三";
				break;
			case 5:
				dayofweek = "周四";
				break;
			case 6:
				dayofweek = "周五";
				break;
			case 7:
				dayofweek = "周六";
				break;
			}
			
			if(i==0)  dayofweek = "今天";
			else if(i==1) dayofweek="明天";
			status =dateFormat.format(c.getTime()); 
			map.put("day",dayofweek);
			map.put("status",status);
			c.add(Calendar.DATE, 1);
			timetable.add(map);
		}
		
		
		return timetable;
	}
	

    public static String formatDate(int year, int month, int day) {
        Date d = new Date(year - 1900, month, day);
        return yearFormat.format(d);
    }

    public static long getDateMills(int year, int month, int day) {
        //Date d = new Date(year, month, day);
		// 1960 4 22
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, day);
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
        return calendar.getTimeInMillis();
    }
	
	public static String getDateString(long time, int type) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long t = currentTime - time;
		long t2 = currentTime - getCurrentDayTime();
		String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "今天  ";
			}else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "昨天  ";
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (y == currentYear) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */" "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日 " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */"  "/*
																 * + (hour < 10
																 * ? "0" + hour
																 * : hour) + ":"
																 * + (minute <
																 * 10 ? "0" +
																 * minute :
																 * minute)
																 */;
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日 "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		}
		return dateStr;
	}

	/**
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

    public static long getActiveTimelong(String result) {
        try {
            Date parse = yearFormat.parse(result);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
    
	/** 
	 * 将时间戳转为代表"距现在多久之前"的字符串 
	 * @param timeStr   时间戳 
	 * @return 
	 */  
	public static String getStandardDate(String timestr) {  
	  
	    StringBuffer sb = new StringBuffer();  
	  
        long t = Long.parseLong(timestr);
	    long time = System.currentTimeMillis() - (t*1000);  
	    long mill = (long) Math.ceil(time /1000);//秒前  
	  
	    long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前  
	  
	    long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时  
	  
	    long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前  
	  
	    if (day - 1 > 0) {  
	        sb.append(day + "天");  
	    } else if (hour - 1 > 0) {  
	        if (hour >= 24) {  
	            sb.append("1天");  
	        } else {  
	            sb.append(hour + "小时");  
	        }  
	    } else if (minute - 1 > 0) {  
	        if (minute == 60) {  
	            sb.append("1小时");  
	        } else {  
	            sb.append(minute + "分钟");  
	        }  
	    } else if (mill - 1 > 0) {  
	        if (mill == 60) {  
	            sb.append("1分钟");  
	        } else {  
	            sb.append(mill + "秒");  
	        }  
	    } else {  
	        sb.append("刚刚");  
	    }  
	    if (!sb.toString().equals("刚刚")) {  
	        sb.append("前");  
	    }  
	    return sb.toString();  
	}
}
