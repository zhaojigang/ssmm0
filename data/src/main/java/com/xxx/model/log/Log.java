package com.xxx.model.log;

import java.io.Serializable;

/**
 * 日志
 */
public class Log implements Serializable {
	private static final long serialVersionUID = -8280602625152351898L;
	
	private String operation;   // 执行的操作
	private String currentTime; // 当前时间

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
}
