package com.xxx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 文件操作工具类
 */
public class PropUtil {
    
    /**
     * 加载属性文件*.properties
     * @param fileName 不是属性全路径名称，而是相对于类路径的名称
     */
    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream is = null;
        
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);//获取类路径下的fileName文件，并且转化为输入流
            if(is != null){
                props = new Properties();
                props.load(is);    //加载属性文件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return props;
    }
    
    /*
     * 从属性文件中获取int型数据
     */
    public static int getInt(Properties props, String key, int defaultValue){
        int value = defaultValue;
        if(props.containsKey(key)){                                //属性文件中是否包含给定键值
            value = NumberUtils.toInt(props.getProperty(key), defaultValue);//从属性文件中取出给定键值的value,并且转换为int型
        }
        return value;
    }
    
    /*
     * 从属性文件中获取long型数据
     */
    public static long getLong(Properties props, String key, long defaultValue){
        long value = defaultValue;
        if(props.containsKey(key)){                                //属性文件中是否包含给定键值
            value = NumberUtils.toLong(props.getProperty(key), defaultValue);//从属性文件中取出给定键值的value,并且转换为int型
        }
        return value;
    }
    
    /*
     * 从属性文件中获取boolean型数据
     */
    public static boolean getBoolean(Properties props, String key, boolean defaultValue){
        boolean value = defaultValue;
        if(props.containsKey(key)){                                //属性文件中是否包含给定键值
            value = toBoolean(props.getProperty(key), defaultValue);
        }
        return value;
    }
    
    
    public static boolean toBoolean(String str, boolean defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(str);
    }
    
    /**
     * 测试
     */
    public static void main(String[] args) {
        Properties props = PropUtil.loadProps("cache_config.properties");
        //System.out.println(props);
        System.out.println(props.getProperty("memcached.servers", "123"));//从属性文件中读取string
        System.out.println(PropUtil.getInt(props, "httpclient.max.conn.per.route2", 10));//属性文件中没有这个key
    }
}