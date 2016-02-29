package com.xxx.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.dao.log.LogDao;
import com.xxx.model.log.Log;

/**
 * 日志service
 */
@Service
public class LogService {
	@Autowired
	private LogDao logDao;

	public boolean addLog(Log log) {
		return logDao.insertLog(log);
	}

}
