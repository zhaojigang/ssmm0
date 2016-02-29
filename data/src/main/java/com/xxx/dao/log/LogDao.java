package com.xxx.dao.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxx.mapper.log.LogMapper;
import com.xxx.model.log.Log;

/**
 * 日志DAO
 */
@Repository
public class LogDao {

	@Autowired
    private LogMapper logMapper;
    /***************注解*****************/
    public boolean insertLog(Log log){
        return logMapper.insertLog(log)==1?true:false;
    }
    
}
