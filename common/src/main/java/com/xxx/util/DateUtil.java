package com.xxx.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 线程安全的日期类工具
 */
public class DateUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();
	
	/**
	 * 获取DateFormat实例
	 */
	public static DateFormat getDateFormat() {
		DateFormat df = threadLocal.get();//从threadLocal中获取当前线程的DateFormat实例副本
		if(df==null){//如果当前线程实例为null，说明该线程第一次使用该方法
			df = new SimpleDateFormat(DATE_FORMAT);//创建df实例
			threadLocal.set(df);//将df实例放置到threadLocal中去
		}
		return df;
	}
	
	/**
	 * 将Date格式化为String字符串
	 */
	public static String formatDate(Date date) {
		return getDateFormat().format(date);
	}
	
	/**
	 * 获取当前时间
	 * @return 字符串（eg.2001-11-12 12:23:34）
	 */
	public static String getCurrentTime() {
		//第一种方式
		//return formatDate(new Date());
		
		//第二种方式（也是最推荐的方式）
		DateFormat df = getDateFormat();
		return df.format(System.currentTimeMillis());
		
		//第三种方式
		/*Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE)
			+"-"+c.get(Calendar.HOUR)+"-"+c.get(Calendar.MINUTE)+"-"+c.get(Calendar.SECOND);*/
	}
	
	/*****************测试*****************/
	/*public static void main(String[] args) {
		System.out.println(getCurrentTime());
	}*/
}
