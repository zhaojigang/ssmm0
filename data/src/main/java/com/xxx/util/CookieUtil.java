package com.xxx.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作相关类
 */
public class CookieUtil {
	
	/**
	 * 增加cookie
	 * @param name		cookie名
	 * @param value		cookie值
	 * @param domain	指定cookie写在哪个域下
	 * @param path		指定cookie存在那个路径下(其实就是一个uri)
	 * @param expiry	指定cookie过期时间
	 */
	public static void addCookie(String name,
								 String value,
								 String domain,
								 String path,
								 int expiry,
								 HttpServletResponse response){
		Cookie cookie = new Cookie(name, value);
		
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setMaxAge(expiry);
		
		response.addCookie(cookie);
	}
	
	/**
	 * 获取cookie
	 * @param request
	 * @param key	将要查找的cookie的名
	 * @return		返回cookie的值
	 */
	public static String getCookie(HttpServletRequest request, String key){
		Cookie[] cookies = request.getCookies();
		/*
		 * 注意在执行循环之前，不需要判断cookies是否为空，因为iterator会在循环前执行hasNext；
		 * 但是，最好判断一下，这样如果cookies为null的话，我们就可以直接返回，不需要执行循环，
		 * 这样就不需要平白的创建一个iterator对象并执行一次hasNext。
		 * 
		 * 下边的判断也可以换成这样CollectionUtils.isEmpty(Arrays.asList(cookies));
		 */
		if(cookies == null || cookies.length == 0){
			return null;
		}
		
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(key)){
				return cookie.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 清空指定cookie
	 * 值得注意的是，清空cookie不是只将相应的cookie的value置空即可，其他信息依旧设置，
	 * 最后加在响应头中，去覆盖浏览器端的相应的cookie
	 */
	public static void removeCookie(String name,
									String domain,
									String path,
									HttpServletResponse response){
		Cookie cookie = new Cookie(name, null);
		
		cookie.setMaxAge(0);
		cookie.setDomain(domain);
		cookie.setPath(path);
		
		response.addCookie(cookie);
	}
}
