package com.bkht.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.MyCodecFactory;
import com.bkht.mina.server.VehicleSocketHandler;
import com.hbdrawn.push.server.TestPush2Clinet;

//import com.hbdrawn.server.ApplicationStartService;

public class MinaServiceStart {

	public Logger logger = LoggerFactory.getLogger(getClass());

	public static IoAcceptor accept = null;

	// public static String localAddress = null;

	public void start() throws IOException {
		accept = new NioSocketAcceptor();
		accept.setHandler(new VehicleSocketHandler());
		DefaultIoFilterChainBuilder chain = accept.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		// TODO 心跳
		// chain.addLast("keep-alive", new MyKeepAliveFilter());
		chain.addLast("codec", new ProtocolCodecFilter(new MyCodecFactory()));

		SocketSessionConfig config = (SocketSessionConfig) accept
				.getSessionConfig();
		config.setReadBufferSize(1024);
		config.setReuseAddress(true);
		config.setUseReadOperation(true);
		InetSocketAddress address = new InetSocketAddress(
				Integer.parseInt("9999"));
		// localAddress = address.getAddress().getHostAddress();
		accept.bind(address);
		logger.info("长连接服务器启动成功");
	}

	public static void main(String[] args) throws IOException {
		MinaServiceStart start = new MinaServiceStart();
		start.start();

		// for test
		TestPush2Clinet client = new TestPush2Clinet();
		client.start();
	}
}
