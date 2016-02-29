package com.xxx.mapper.log;

import org.apache.ibatis.annotations.Insert;

import com.xxx.model.log.Log;

/**
 * 日志Mapper
 */
public interface LogMapper {
	
	/**
	 * 这里需要注意的是，current_time是数据库的保留参数，两点注意：
	 * 1、最好不要用保留参数做变量名
	 * 2、如果不经意间已经用了，那么保留参数需要用``括起来（`-->该符号是英文状态下esc键下边的那个键）
	 * @param log
	 * @return
	 */
	@Insert("INSERT INTO log(operation, `current_time`) VALUES(#{operation},#{currentTime})")
	public int insertLog(Log log);

}
