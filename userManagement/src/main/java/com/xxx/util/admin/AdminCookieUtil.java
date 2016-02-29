package com.xxx.util.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.xxx.model.userManagement.Admin;
import com.xxx.util.AESUtil;
import com.xxx.util.CookieUtil;

/**
 * Admin的cookie操作类
 */
public class AdminCookieUtil {
	private static final String COOKILE_NAME = "allinfo";
	private static final String USER_NAME = "username";
	private static final String DOMAIN = "";//when working on localhost the cookie-domain must be set to "" or NULL or FALSE
	private static final String PATH = "/";
	private static final int EXPIRY = -1;//关闭浏览器，则cookie消失
	
	private static final String ENCRYPT_KEY = "gEfcsJx8VUT406qI4r6/3104noOzI/YAaS98cToY+nI=";//加解密密钥
	 
	public static void addLoginCookie(Admin admin, HttpServletResponse response){
		try{
			CookieUtil.addCookie(COOKILE_NAME, AESUtil.encrypt(admin.toJson(), Base64.decodeBase64(ENCRYPT_KEY)), DOMAIN, PATH, EXPIRY, response);
			CookieUtil.addCookie(USER_NAME, admin.getUsername(), DOMAIN, PATH, EXPIRY, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Admin getLoginCookie(HttpServletRequest request){
		String json = CookieUtil.getCookie(request, COOKILE_NAME);
		if(StringUtils.isNotBlank(json)){
			try{
				return Admin.parseJsonToAdmin(AESUtil.decrypt(json, Base64.decodeBase64(ENCRYPT_KEY)));
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
