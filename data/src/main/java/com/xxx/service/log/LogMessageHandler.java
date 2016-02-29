package com.xxx.service.log;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxx.dao.log.LogDao;
import com.xxx.model.log.Log;
import com.xxx.rpc.mq.handler.MessageHandler;


/**
 * 日志处理器（更适合放在data层）
 * 因为：
 * 1、data依赖于rpc，而rpc不依赖于data，所以如果该类放在rpc层，并且该类需要用到数据库操作（eg.将日志写入数据库），那么就不好办了
 * 2、rpc层说白了，就是一些rpc工具类，实际上与业务无关，与业务有关的，我们可以抽取到该部分来
 */
@Component
public class LogMessageHandler implements MessageHandler {

	@Autowired
	private LogDao logDao;
	
	public void handle(Message message) {
		System.out.println(logDao);
		ObjectMessage objMsg = (ObjectMessage)message;
		try {
			Log log = (Log)objMsg.getObject();
			logDao.insertLog(log);//将日志写入数据库
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
