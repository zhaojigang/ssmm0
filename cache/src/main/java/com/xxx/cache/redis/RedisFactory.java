package com.xxx.cache.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;

import com.xxx.util.PropUtil;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisFactory {
	private static ShardedJedisPool jedisPool = null;
	/**
	 * 构建ShardJedisPool
	 * 一个ShardJedisPool中配置了多个JedisShardInfo
	 * 每一个JedisShardInfo都是一个server
	 * 一个ShardJedisPool中可以获取多个ShardJedis连接实例，具体数目由maxTotal属性而定
	 * 注意：
	 * 1、这里只有一个ShardJedisPool，如果你有很多业务，而且不想这些业务都共用几台redis服务器的话，
	 * 	    你可以创建多个ShardJedisPool,每个pool中放置不同的服务器即可
	 * 2、这时候多个ShardJedisPool可以放置在一个hashmap中，key由自己指定（写在一个Enum类中去），key的名称一般与业务挂钩就好
	 */
	static{
		Properties props = PropUtil.loadProps("cache_config.properties");//加载属性文件
		/*
		 * 从属性文件读取参数
		 */
		String servers = props.getProperty("redis.servers", "127.0.0.1:6379");
		String[] serverArray = servers.split(" ");//获取服务器数组
		
		int timeout = PropUtil.getInt(props, "redis.timeout", 5000);//默认：2000ms(超时时间：单位ms)
		boolean lifo = PropUtil.getBoolean(props, "redis.conf.lifo", true);//默认：true
		
		int maxTotal = PropUtil.getInt(props, "redis.conf.maxTotal", 64);//默认：8个(最多创建几个ShardJedis，即连接)
		boolean blockWhenExhausted = PropUtil.getBoolean(props, "redis.conf.blockWhenExhausted", true);//默认：true(连接耗尽是否阻塞等待)
		long maxWaitMillis = PropUtil.getLong(props, "redis.conf.maxWaitMillis", -1);//默认：-1，即无限等待(等待获取连接的最长时间)
		
		boolean testOnBorrow = PropUtil.getBoolean(props, "redis.conf.testOnBorrow", false);//默认：false(获取连接前，是否对连接进行测试)
		boolean testOnReturn = PropUtil.getBoolean(props, "redis.conf.testOnReturn", false);//默认：false(归还连接前，是否对连接进行测试)
		
		int maxIdle = PropUtil.getInt(props, "redis.conf.maxIdle", 8);//默认：8(最大空闲连接数)
		int minIdle = PropUtil.getInt(props, "redis.conf.minIdle", 0);//默认：0(最小空闲连接数)
		boolean testWhileIdle = PropUtil.getBoolean(props, "redis.conf.testWhileIdle", true);//默认：false(对空闲连接进行扫描，检查连接有效性)
		long timeBetweenEvictionRunsMillis = PropUtil.getLong(props, "redis.conf.timeBetweenEvictionRunsMillis", 30000);//默认：-1，(两次扫描空闲连接的时间间隔)
		int numTestsPerEvictionRun = PropUtil.getInt(props, "redis.conf.numTestsPerEvictionRun", 3);//默认：3(每次空闲扫描时扫描的控线连接的个数)
		long minEvictableIdleTimeMillis = PropUtil.getLong(props, "redis.conf.minEvictableIdleTimeMillis", 60000);//默认：30min(一个空闲连接至少连续保持30min中空闲才会被空闲扫描)
		/*
		 * 配置redis参数
		 */
		JedisPoolConfig config = new JedisPoolConfig();
		config.setLifo(lifo);//(last in, first out)是否启用后进先出，默认true
		/*
		 * 即原来的maxActive,能够同时建立的最大连接个数（就是最多分配多少个ShardJedis实例）,
		 * 默认8个，若设置为-1，表示为不限制，
		 * 如果pool中已经分配了maxActive个jedis实例，则此时pool的状态就成exhausted了
		 * 
		 * 这里最多可以生产64个shardJedis实例
		 */
		config.setMaxTotal(maxTotal);
		config.setBlockWhenExhausted(blockWhenExhausted);//连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true, 达到maxWait时抛出JedisConnectionException
		config.setMaxWaitMillis(maxWaitMillis);//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		
		config.setTestOnBorrow(testOnBorrow);//使用连接时，先检测连接是否成功,若为true，则获取到的shardJedis连接都是可用的，默认false
		config.setTestOnReturn(testOnReturn);//归还连接时，检测连接是否成功
		
		/*
		 * 空闲状态
		 */
		config.setMaxIdle(maxIdle);//空闲连接数（即状态为idle的ShardJedis实例）大于maxIdle时，将进行回收,默认8个
		config.setMinIdle(minIdle);//空闲连接数小于minIdle时，创建新的连接,默认0
		/*
		 * 在空闲时检查有效性, 默认false,如果为true，表示有一个idle object evitor线程对idle object进行扫描，
		 * 如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		 */
		config.setTestWhileIdle(testWhileIdle);
		config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);//表示idle object evitor两次扫描之间要sleep的毫秒数；
		config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);//表示idle object evitor每次扫描的最多的对象数；
		//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		
		/*
		 * 构建JedisShardInfo集合
		 */
		List<JedisShardInfo> jedisList = new ArrayList<JedisShardInfo>(1);//我这里只有一台机器，所以传入参数1，否则默认为10，浪费空间
		for(String server : serverArray){
			String[] hostAndPort = server.split(":");
			/*
			 * 这句代码中我没有判断hostAndPort是不是长度为2，而且端口如果没有指定或指定错误的话，就直接转到6379
			 * 实际中，我们在配置服务器的时候就一定要注意配置格式正确：host:port
			 */
			JedisShardInfo shardInfo = new JedisShardInfo(hostAndPort[0], 
														  NumberUtils.toInt(hostAndPort[1], 6379), 
														  timeout);
			jedisList.add(shardInfo);
		}
		/*
		 * 创建ShardJedisPool
		 */
		jedisPool = new ShardedJedisPool(config, jedisList);//构建jedis池
	}
	
	/**
	 * 如果有多个ShardJedisPool，则需要写一个hash算法从hashmap中选一个pool返回
	 */
	public static ShardedJedisPool getJedisPool() {
		return jedisPool;
	}
}