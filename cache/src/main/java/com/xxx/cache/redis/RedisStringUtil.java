package com.xxx.cache.redis;

import redis.clients.jedis.ShardedJedis;

import com.xxx.cache.util.CachePrefix;

/**
 * 字符串缓存操作类或者JavaBean缓存操作类
 * key String, value String-->看下边的注意点2
 * key byte[], value byte[]-->key.getBytes[], value 序列化为byte[]，通常需要自己写一个序列化工具
 * 注意：这一点与memcached不一样，memcached可以key String, value Object
 * 1、memcached直接加序列化器就可以，或者在业务层中将Object-->String
 * 2、redis执行此接口，一般只会采用后者Object-->String
 */
public class RedisStringUtil extends RedisBaseUtil{
	private static final String KEY_SPLIT = "-";//用于隔开缓存前缀与缓存键值
	/**
	 * 设置缓存
	 * 类似于memcached的set，不管是否已经有相同的key，都成功
	 * 实际上只是set(String, String)
	 */
	public static void set(CachePrefix keyPrefix, String key, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.set(keyPrefix+KEY_SPLIT+key, value);//set(String,String),value除了string以外，还可以是byte[]
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 设置缓存，并指定缓存过期时间，单位是秒
	 */
	public static void setex(CachePrefix keyPrefix, String key, String value, int expire){
		boolean broken = false;//该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.setex(keyPrefix+KEY_SPLIT+key, expire, value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 设置缓存，如果设置的key不存在，直接设置，如果key已经存在了，则什么操作都不做，直接返回
	 * 类似于memcached的add
	 */
	public static boolean setnx(CachePrefix keyPrefix, String key, String value){
		boolean broken = false;//该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return false;
			}
			long setCount = jedis.setnx(keyPrefix+KEY_SPLIT+key, value);
			if(setCount == 1){
				return true;
			}
			return false;
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return false;
	}
	
	/**
	 * 根据key获取缓存
	 * @param key
	 * @return String
	 */
	public static String get(CachePrefix keyPrefix, String key){
		boolean broken = false;//该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.get(keyPrefix+KEY_SPLIT+key);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 删除缓存
	 */
	public static void delete(CachePrefix keyPrefix, String key){
		boolean broken = false;//该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.del(keyPrefix+KEY_SPLIT+key);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 更新缓存过期时间，单位：秒
	 * 从运行该方法开始，为相应的key-value设置缓存过期时间expire
	 * 类似于memcached中的touch命令
	 */
	public static void setExpire(CachePrefix keyPrefix, String key, int expire){
		boolean broken = false;
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.expire(keyPrefix+KEY_SPLIT+key, expire);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		//System.out.println(get("hello"));
		//delete("hello");
		//setex("hello1", "word1", 1);
		//setExpire("hello1", 20);
		//System.out.println(get("hello1"));
	}

}
