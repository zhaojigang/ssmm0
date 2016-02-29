package com.xxx.web.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xxx.rpc.mq.util.ActiveMQP2PUtil;
import com.xxx.service.log.LogMessageHandler;

/**
 * 用于接收消息的测试类
 */
@Controller
@RequestMapping("/mq")
public class MessageReceiver {
	
	@Autowired
	private LogMessageHandler handler;
	
	@RequestMapping("/receive")
	public void receiveMessage() {
		ActiveMQP2PUtil.receiveMessage(handler);
	}
	
}
