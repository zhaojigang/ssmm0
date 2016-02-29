package com.xxx.rpc.mq.util;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.xxx.rpc.mq.handler.MessageHandler;
import com.xxx.util.PropUtil;

/**
 * activemq p2p 工具类 
 */
public class ActiveMQP2PUtil {
	private static final String RPC_CONFIG_FILE = "rpc_config.properties";
	private static String queueURL;		//队列所在的URL
	private static String queueName;	//队列名称
	private static ConnectionFactory connectionFactory;	//连接工厂
	
	static{
		Properties props = PropUtil.loadProps(RPC_CONFIG_FILE);
		queueURL = props.getProperty("activemq.queueURL", "tcp://127.0.0.1:61616");
		System.out.println(queueURL);
		queueName = props.getProperty("activemq.queueName", "adminQueue");
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, 
														  ActiveMQConnection.DEFAULT_PASSWORD, 
														  queueURL);
	}
	
	/**
	 * 发送消息
	 */
	public static void sendMessage(Serializable message){
		Connection conn = null;
		try {
			conn = connectionFactory.createConnection();//创建连接
			conn.start();//启动连接
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);//创建session
			Destination destination = session.createQueue(queueName);//创建队列
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//消息设置为非持久化
			ObjectMessage msg = session.createObjectMessage(message);//创建消息：createObjectMessage()该方法的入参是Serializable型的
			producer.send(msg);//发送消息
			session.commit();//提交消息
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 接收消息
	 * @param handler 自定义的消息处理器
	 */
	public static void receiveMessage(MessageHandler handler){
		Connection conn = null;
		try {
			conn = connectionFactory.createConnection();//创建连接
			conn.start();//启动连接
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);//创建session
			Destination destination = session.createQueue(queueName);//创建队列
			MessageConsumer consumer = session.createConsumer(destination);//创建消息消费者
			while(true){//死循环接收消息
				Message msg = consumer.receive();//接收消息
				if(msg!=null){
					handler.handle(msg);//处理消息
					//System.out.println(msg);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*public static void main(String[] args) {
		sendMessage("hello world3");
	}*/
}
