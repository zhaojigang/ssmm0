package com.xxx.cache.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 获取ShardJedis与归还实例 
 */
public class RedisBaseUtil {
	/**
	 * 从ShardJedisPool中获取ShardJedis
	 */
	public static ShardedJedis getJedis(){
		ShardedJedisPool jedisPool = RedisFactory.getJedisPool();//获取连接池
		if(jedisPool == null){
			return null;
		}
		return jedisPool.getResource();
	}
	
	/**
	 * 归还jedis实例到连接池中
	 */
	public static void returnJedis(ShardedJedis jedis, boolean broken){
		if(jedis==null){//如果传入的jedis是null的话，不需要归还
			return;
		}
		ShardedJedisPool jedisPool = RedisFactory.getJedisPool();//获取连接池
		if(jedisPool == null){//如果连接池为null的话，不需要归还
			return;
		}
		if(broken){//如果为true的话，表示是因为发生了异常才归还
			jedisPool.returnBrokenResource(jedis);
			return;
		}
		jedisPool.returnResource(jedis);//缓存正常操作结束之后，归还jedis
	}
}
