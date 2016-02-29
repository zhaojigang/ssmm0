package com.xxx.rpc.mq.handler;

import javax.jms.Message;

/**
 * 消息处理器接口
 */
public interface MessageHandler {
	public void handle(Message message);
}
