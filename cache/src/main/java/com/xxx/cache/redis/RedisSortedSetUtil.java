package com.xxx.cache.redis;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

/**
 * sorted set缓存操作类 
 * 1、有序集合，最后的顺序是按照score从小到大的顺序排列
 * 2、元素不能重复
 * 3、没有从set中获取指定value的运算
 */
public class RedisSortedSetUtil extends RedisBaseUtil {
	/***************************添加缓存*****************************/
	/**
	 * 添加缓存(一个)
	 * @param sortedSet 添加入的集合
	 * @param score		权重
	 * @param value		值
	 */
	public static void zadd(String sortedSet,double score, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zadd(sortedSet, score, value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 添加缓存(一次可添加多个)
	 * @param sortedSet		添加入的集合
	 * @param value2score	加入集合的元素集
	 */
	public static void zadd(String sortedSet,Map<String, Double> value2score){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zadd(sortedSet, value2score);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/***************************获取缓存*****************************/
	/**
	 * 返回sortedSet内[start,end]索引的元素set
	 * 1、在sortedSet中，元素是按照score从小到大排列的，
	 * 	    此方法从前向后获取元素（即按元素的score从小到大排列）
	 * @param sortedSet
	 * @param start
	 * @param end
	 */
	public static Set<String> zrange(String sortedSet, long start, long end){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.zrange(sortedSet, start, end);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 返回sortedSet内所有元素，元素按照score从小到大排列
	 */
	public static Set<String> zrange(String sortedSet){
		return zrange(sortedSet, 0, -1);
	}
	
	/**
	 * 返回sortedSet集合[start, end]中的元素
	 * 1、此方法相当于从后向前取元素，即元素从大到小排列
	 * 或者相当于将sortedSet从大到小排列，然后从前向后去元素
	 * @param sortedSet
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<String> zrevrange(String sortedSet, long start, long end){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.zrevrange(sortedSet, start, end);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 返回sortedSet内所有元素，元素按照score从大到小排列
	 */
	public static Set<String> zrevrange(String sortedSet){
		return zrevrange(sortedSet, 0, -1);
	}
	
	/**
	 * 获取sortedSet内[minScore, maxScore]的元素
	 * @param sortedSet
	 * @param minScore
	 * @param maxScore
	 * @return
	 */
	public static Set<String> zrangeByScore(String sortedSet, double minScore, double maxScore){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.zrangeByScore(sortedSet, minScore, maxScore);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/**
	 * 获取Set<Tuple>集合，其中Tuple是value与score的结构体
	 * @param sortedSet
	 * @param minScore
	 * @param maxScore
	 * @return
	 */
	public static Set<Tuple> zrevrangeByScoreWithScores(String sortedSet, double minScore, double maxScore){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return null;
			}
			return jedis.zrevrangeByScoreWithScores(sortedSet, maxScore, minScore);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return null;
	}
	
	/***************************删除缓存*****************************/
	/**
	 * 删除多个缓存
	 * @param sortedSet
	 * @param values
	 */
	public static void zremove(String sortedSet, String... values){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zrem(sortedSet, values);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 删除指定范围（按照索引，包前包后）的缓存
	 * @param sortedSet
	 * @param start
	 * @param end
	 */
	public static void zremrangeByRank(String sortedSet, long start, long end){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zremrangeByRank(sortedSet, start, end);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/**
	 * 删除指定范围（按照分数，包前包后）的缓存
	 * @param sortedSet
	 * @param minScore
	 * @param maxScore
	 */
	public static void zremrangeByScore(String sortedSet, double minScore, double maxScore){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zremrangeByScore(sortedSet, minScore, maxScore);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
	}
	
	/***************************其他*****************************/
	/**
	 * 获取集合sortedSet的长度
	 * @param sortedSet
	 * @return
	 */
	public static long zlength(String sortedSet){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return 0;
			}
			return jedis.zcard(sortedSet);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return 0;
	}
	
	/**
	 * 获取sortedSet中的value的权重score
	 * @param sortedSet
	 * @param value
	 * @return
	 */
	public static double zscore(String sortedSet, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return 0;
			}
			return jedis.zscore(sortedSet, value);
		} catch (Exception e) {
			broken = true;
		}finally{
			returnJedis(jedis, broken);
		}
		return 0;
	}
	
	/**
	 * 为sortedSet中的value的权重加上增量score
	 * @param sortedSet
	 * @param score
	 * @param value
	 */
	public static void zincrby(String sortedSet,double score, String value){
		boolean broken = false;//标记：该操作是否被异常打断而没有正常结束
		ShardedJedis jedis = null;
		try {
			jedis = getJedis();//获取jedis实例
			if(jedis==null){
				broken = true;
				return;
			}
			jedis.zincrby(sortedSet, score, value);
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
		zadd("aaset", 1, "aa");
		zadd("aaset", 4, "bb");
		zadd("aaset", 0, "cc");
		
		System.out.println(zrange("aaset", 0, -1));//[cc, aa, bb]
		System.out.println(zrange("aaset", 0, 1));//[cc, aa]
		System.out.println(zrevrange("aaset", 0, -1));//[bb, aa, cc]
		System.out.println(zrevrange("aaset", 0, 1));//[bb, aa]
		System.out.println(zrangeByScore("aaset", 0, 2));//[cc, aa]
		Set<Tuple> s = zrevrangeByScoreWithScores("aaset", 0, 2);
		for(Tuple t : s){
			System.out.println(t.getElement()+"-->"+t.getScore());//aa-->1.0  cc-->0.0
		}
		
		System.out.println(zlength("aaset"));//3
		System.out.println(zscore("aaset","bb"));//4.0
		zincrby("aaset",10,"bb");
		System.out.println(zscore("aaset","bb"));//14.0
		zremove("aaset", "cc");
		System.out.println(zrange("aaset"));//[aa, bb]
		zremrangeByScore("aaset", 10, 20);
		System.out.println(zrange("aaset"));//[aa]
	}

}
