package com.xxx.cache.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.ShardedJedis;

/**
 * hash缓存操作类
 */
public class RedisHashUtil extends RedisBaseUtil {
	/***************************添加缓存*****************************/
	/**
	 * 添加单个缓存key-value到map中
	 */
	public static void hset(String map, String key, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.hset(map, key, value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 添加单个缓存key-value到map中
	 * 若已经存在于指定key相同的key，那么就不操作
	 */
	public static void hsetnx(String map, String key, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.hsetnx(map, key, value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 在map中添加key2value的map，即一次性添加多条缓存
	 * @param map
	 * @param key2value
	 */
	public static void hmset(String map, Map<String, String> key2value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.hmset(map, key2value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/***************************获取缓存*****************************/
	/**
	 * 获取map中key的集合
	 * @param set
	 */
	public static Set<String> hkeys(String map){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.hkeys(map);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 获取map中的所有key的value
	 */
	public static List<String> hvals(String map){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.hvals(map);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 从map中获取多个key的value，并放在List集合中
	 */
	public static List<String> hmget(String map, String... keys){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.hmget(map, keys);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 从map中获取全部的缓存key-value对
	 */
	public static Map<String, String> hgetAll(String map){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.hgetAll(map);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 从map中获取相应key的缓存value
	 */
	public static String hget(String map, String key){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.hget(map, key);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/***************************删除缓存*****************************/
	/**
	 * 从map中删除多个缓存
	 */
	public static void hdel(String map, String... keys){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.hdel(map, keys);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/***************************其他*****************************/
	/**
	 * 获取map中的key-value数
	 */
	public static long hlen(String map){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return 0;
			}
			return jedis.hlen(map);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return 0;
	}
	
	/**
	 * map中是否存在键为key的缓存
	 */
	public static boolean hexists(String map, String key){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return false;
			}
			return jedis.hexists(map, key);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return false;
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		hset("aamap", "aa1", "aa11");
		Map<String,String> maps = new HashMap<String, String>();
		maps.put("aa2", "aa22");
		maps.put("aa3", "aa33");
		hmset("aamap", maps);
		
		System.out.println(hkeys("aamap"));//[aa3, aa2, aa1]
		System.out.println(hvals("aamap"));//[aa33, aa22, aa11]
		System.out.println(hgetAll("aamap"));//{aa3=aa33, aa2=aa22, aa1=aa11}
		System.out.println(hget("aamap","aa2"));//aa22
		System.out.println(hmget("aamap","aa2","aa1"));//[aa22, aa11]
		
		System.out.println(hlen("aamap"));//3
		System.out.println(hexists("aamap","aa3"));//true
		System.out.println(hexists("aamap","aa0"));//false
		
		hdel("aamap","aa0");
		hdel("aamap","aa1");
		System.out.println(hgetAll("aamap"));//{aa3=aa33, aa2=aa22}
		
	}
}
