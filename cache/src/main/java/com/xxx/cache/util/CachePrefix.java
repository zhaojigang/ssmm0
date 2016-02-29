package com.xxx.cache.util;

/**
 * 在该类中定义一些缓存的前缀
 * 方式：
 * 1、定义一个类，在其中添加静态常量
 * 2、使用枚举类（这是最好的方式）
 * 作用：
 * 1、防止缓存键值重复（通常情况下，每一种业务对应一种前缀）
 * 2、可以起到根据前缀分类的作用
 * 后者主要是在memcached中实现类似于redis的实现。
 */
public enum CachePrefix {
	USER_MANAGEMENT,	//人员管理业务类缓存前缀
	HOTEL_MANAGEMENT;	//酒店管理业务类缓存前缀
}
