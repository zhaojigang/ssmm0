package com.xxx.cache.memcached;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.lang3.StringUtils;

import com.xxx.cache.util.CachePrefix;
import com.xxx.util.PropUtil;

/**
 * memcached工具类（基于Xmemcached实现）
 */
public class MemcachedUtil {
	private static Map<Integer, MemcachedClient> clientMap 
						= new HashMap<Integer, MemcachedClient>();//client的集合
	private static int maxClient = 3;
	private static int expireTime = 900;//900s（默认的缓存过期时间）
	private static int maxConnectionPoolSize = 1;//每个客户端池子的连接数
	private static long op_time = 2000L;//操作超时时间
	
	private static final String KEY_SPLIT = "-";//用于隔开缓存前缀与缓存键值
	
	/**
	 * 构建MemcachedClient的map集合
	 */
	static{
		//读取配置文件
		Properties props = PropUtil.loadProps("cache_config.properties");
		String servers = props.getProperty("memcached.servers", "127.0.0.1:11211");//获取memcached servers集合
		maxClient = PropUtil.getInt(props, "", maxClient);
		expireTime = PropUtil.getInt(props, "memcached.expiretime", expireTime);
		maxConnectionPoolSize = PropUtil.getInt(props, "memcached.connection.poolsize", maxConnectionPoolSize);
		op_time = PropUtil.getLong(props, "memcached.op.timeout", op_time);
		
		if(StringUtils.isNotBlank(servers)){
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers));
			/*
			 * 下面这样是配置主从
			 * 其中localhost:11211是主1，localhost:11212是他的从
			 * host2:11211是主2，host2:11212是他的从
			 * 
			 * 注意：使用主从配置的前提是builder.setFailureMode(true)
			 */
			//MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddressMap("127.0.0.1:11211,127.0.0.1:11212"));
			//builder.setConnectionPoolSize(1);//这个默认也是1
			builder.setSessionLocator(new KetamaMemcachedSessionLocator(true));//使用一致性hash算法
			
			SerializingTranscoder transcoder = new SerializingTranscoder(1024*1024);//序列化转换器，指定最大的数据大小1M
			//transcoder.setCharset("UTF-8");//默认为UTF-8，这里可去掉
			transcoder.setCompressionThreshold(1024*1024);//单位：字节，压缩边界值，任何一个大于该边界值（这里是：1M）的数据都要进行压缩
			//transcoder.setCompressionMode(CompressionMode.GZIP);//压缩算法,默认就是GZIP,这里也可以去掉
			
			builder.setTranscoder(transcoder);
			builder.setCommandFactory(new BinaryCommandFactory());//命令工厂
			//builder.setFailureMode(true);//设置failure模式
			//构建10个MemcachedCient,并放入clientMap
			for(int i=0;i<maxClient;i++){
				try {
					MemcachedClient client = builder.build();
					client.setOpTimeout(op_time);//设置操作超时时间，注释写的默认为1s，但是查看源代码默认是5s
					//client.setSanitizeKeys(true);//URL做key
					clientMap.put(i, client);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 从MemcachedClient中取出一个MemcachedClient
	 */
	public static MemcachedClient getMemcachedClient(){
		/*
		 * Math.random()：产生[0,1)之间的小数
		 * Math.random()*maxClient：[0~maxClient)之间的小数
		 * (int)(Math.random()*maxClient)：[0~maxClient)之间的整数
		 */
		return clientMap.get((int)(Math.random()*maxClient));
	}
	
	/**
	 * 设置缓存
	 * @param keyPrefix    缓存的键的前缀
	 * @param key    	         缓存的键
	 * @param value  	         缓存的值
	 * @param exp	  	         缓存过期时间
	 */
	public static void setCacheWithNoReply(CachePrefix keyPrefix, 
										   String key, 
										   Object value, 
										   int exp){
		try {
			MemcachedClient client = getMemcachedClient();
			client.addWithNoReply(keyPrefix+KEY_SPLIT+key, exp, value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置缓存
	 * @param exp	   缓存过期时间（默认时间）
	 */
	public static void setCacheWithNoReply(CachePrefix keyPrefix,
										   String key,
										   Object value){
		setCacheWithNoReply(keyPrefix, key, value, expireTime);
	}
	
	/**
	 * 设置缓存，并返回缓存成功与否
	 * 注意：
	 * 1、设置已经设置过的key-value，将会返回false
	 */
	public static boolean setCache(CachePrefix keyPrefix,
								   String key,
								   Object value,
								   int exp){
		boolean setCacheSuccess = false; 
		try {
			MemcachedClient client = getMemcachedClient();
			setCacheSuccess = client.add(keyPrefix+KEY_SPLIT+key, exp, value);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return setCacheSuccess;
	}
	
	/**
	 * 设置缓存，并返回缓存成功与否（缓存超时时间采用默认）
	 * @param key
	 * @param value
	 */
	public static boolean setCache(CachePrefix keyPrefix,
								   String key, 
								   Object value){
		return setCache(keyPrefix, key, value, expireTime);
	}
	
	/**
	 * 获取缓存
	 */
	public static Object getCache(CachePrefix keyPrefix, String key){
		Object value = null;
		try {
			MemcachedClient client = getMemcachedClient();
			value = client.get(keyPrefix+KEY_SPLIT+key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 删除缓存
	 */
	public static void removeCacheWithNoReply(CachePrefix keyPrefix, String key){
		try {
			MemcachedClient client = getMemcachedClient();
			client.deleteWithNoReply(keyPrefix+KEY_SPLIT+key);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除缓存,并返回删除成功与否
	 */
	public static boolean removeCache(CachePrefix keyPrefix, String key){
		boolean removeCacheSuccess = false; 
		try {
			MemcachedClient client = getMemcachedClient();
			removeCacheSuccess = client.delete(keyPrefix+KEY_SPLIT+key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return removeCacheSuccess;
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		/*for(int i=0;i<100;i++){
			System.out.println(Math.random());
		}*/
		System.out.println(MemcachedUtil.setCache(CachePrefix.USER_MANAGEMENT,"nana", "zhaojigang"));
		System.out.println(MemcachedUtil.getCache(CachePrefix.USER_MANAGEMENT,"nana"));
		/*System.out.println(MemcachedUtil.getCache("hello2"));
		System.out.println(MemcachedUtil.getCache("hello2"));
		System.out.println(MemcachedUtil.getCache("hello2"));
		System.out.println(MemcachedUtil.getCache("hello2"));
		System.out.println(MemcachedUtil.getCache("hello2"));*/
	}
}
