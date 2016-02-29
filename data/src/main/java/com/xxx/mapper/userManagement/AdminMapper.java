package com.xxx.mapper.userManagement;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.xxx.model.userManagement.Admin;

/**
 * 管理员Mapper
 */
public interface AdminMapper {
	
	/**************注解**************/
	@Insert("INSERT INTO userinfo(username, password) VALUES(#{username},#{password})")
	public int insertAdmin(Admin admin);

	@Select("SELECT * FROM userinfo WHERE username = #{username} AND password = #{password}")
	@Results(value = { 
			@Result(id = true, column = "id", property = "id"),
			@Result(column = "username", property = "username"),
			@Result(column = "password", property = "password") })
	public Admin selectAdmin(@Param("username") String username,
							 @Param("password") String password);
	
	/***************xml**************/
	/**
	 * 条件不定式查询
	 * 我们这里使用@Param指定参数，这样的话，在AdminMapper.xml中就不用再使用parameterType属性了；否则得写parameterType属性
	 */
	public List<Admin> getAdminByConditions(@Param("username")String username,
											@Param("password")String password, 
											@Param("start")int start, 
											@Param("limit")int limit);
	
	/**
	 * 返回主键
	 */
	public int insertAdminWithBackId(Admin admin);
	
	/****************guava cache*****************/
	@Select("SELECT * FROM userinfo WHERE username = #{username}")
	@Results(value = { 
			@Result(id = true, column = "id", property = "id"),
			@Result(column = "username", property = "username"),
			@Result(column = "password", property = "password") })
	public List<Admin> getUserByName(@Param("username") String username);
	
	/**************memcached**************/

	@Select("SELECT * FROM userinfo WHERE id = #{id}")
	@Results(value = { 
			@Result(id = true, column = "id", property = "id"),
			@Result(column = "username", property = "username"),
			@Result(column = "password", property = "password") })
	public Admin selectById(@Param("id") int id);
}
