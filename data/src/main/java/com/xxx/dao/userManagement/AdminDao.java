package com.xxx.dao.userManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxx.mapper.userManagement.AdminMapper;
import com.xxx.model.userManagement.Admin;

/**
 * 管理员DAO
 */
@Repository
public class AdminDao {
	@Autowired
    private AdminMapper adminMapper;
    /***************注解*****************/
    public boolean register(Admin admin){
        return adminMapper.insertAdmin(admin)==1?true:false;
    }
    
    public Admin login(String username ,String password){
        return adminMapper.selectAdmin(username, password);
    }
    /****************xml******************/
    public List<Admin> findAdmin(String username, String password, int start, int limit){
    	return adminMapper.getAdminByConditions(username, password, start, limit);
    }
    
    public int insertAdminWithBackId(Admin admin){
    	return adminMapper.insertAdminWithBackId(admin);
    }
    /******************guava cache********************/
    public List<Admin> getUserByName(String username){
    	return adminMapper.getUserByName(username);//这里即使返回的list集合为空，也是[]，而非null
    }
    /******************memcached********************/
    public Admin getUserById(int id){
    	return adminMapper.selectById(id);
    }
}
