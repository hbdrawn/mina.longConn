package com.bkht.mina.utils;

import java.util.Properties;

public class SysConstant {
	public static int mina_id;
	public static int mina_queue_maxThread;

	public static int vehicle_socket_port;
	public static int vehicle_socket_bufferSize;
	public static int vehicle_socket_heartbreat_timeout;
	public static int vehicle_socket_idleTime = 10000;

	public static int server_socket_port;
	public static int server_socket_bufferSize;
	public static int server_socket_heartbreat_timeout;
	public static int server_socket_idleTime = 10000;

	// tags
	public static byte tag_v_conn = 1;
	public static byte tag_v_test = 99;
	public static byte tag_v_test_reply = 100;

	public static byte tag_server_conn = 101;
	public static byte tag_server_conn_reply = 102;
	public static byte tag_server_heart = 103;
	public static byte tag_server_heart_reply = 104;
	public static byte tag_server_test = 105;
	public static byte tag_server_test_reply = 106;

	public static byte tag_mina_request_close = 126; // close mina
	public static byte tag_mina_closed = 127; // close mina

	public static long message_timeout = 180000; // 3 min

	public static String package_path = null;

	/**
	 * 初始化系统属性
	 * 
	 * @param prop
	 *            读取文件的属性
	 */
	public static void init(Properties prop) {
		// // 部署mina服务器的id 用于分布式式部署服务器
		// mina_id = Integer.parseInt(prop.getProperty("mina.id"));
		//
		// // 车辆的缓冲区大小
		// vehicle_socket_bufferSize = Integer.parseInt(prop
		// .getProperty("vehicle.socket.bufferSize"));
		// // 车辆端口
		// vehicle_socket_port = Integer.parseInt(prop
		// .getProperty("vehicle.socket.port"));
		// // 车辆心跳超时时间
		// vehicle_socket_heartbreat_timeout = Integer.parseInt(prop
		// .getProperty("vehicle.socket.heartbreat.timeout"));
		//
		// // 服务器端缓冲区大小
		// server_socket_bufferSize = Integer.parseInt(prop
		// .getProperty("server.socket.bufferSize"));
		// // 服务器端端口
		// server_socket_port = Integer.parseInt(prop
		// .getProperty("server.socket.port"));
		// // 心跳超时时间
		// server_socket_heartbreat_timeout = Integer.parseInt(prop
		// .getProperty("server.socket.heartbreat.timeout"));
		//
		// // 最大线程个数 用于以后多个服务器时使用 线程池的消息发送
		// mina_queue_maxThread = Integer.parseInt(prop
		// .getProperty("mina.queue.maxThread"));
		//
		// message_timeout =
		// Integer.parseInt(prop.getProperty("message.timeout"));

		package_path = prop.getProperty("package.path");
	}
}
