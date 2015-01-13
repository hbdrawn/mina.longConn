package com.hbdrawn.push.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.hbdrawn.server.ApplicationStartService;

public class LongConnectionService {

	public Logger logger = LoggerFactory.getLogger(getClass());

	public static IoAcceptor accept = null;

	// public static String localAddress = null;

	public void start() throws IOException {
		accept = new NioSocketAcceptor();
		accept.setHandler(new LongConnHandler());
		DefaultIoFilterChainBuilder chain = accept.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		// 心跳
		chain.addLast("keep-alive", new MyKeepAliveFilter());
		chain.addLast("codec", new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));

		SocketSessionConfig config = (SocketSessionConfig) accept
				.getSessionConfig();
		config.setReadBufferSize(2048);
		config.setReuseAddress(true);
		config.setUseReadOperation(true);
		InetSocketAddress address = new InetSocketAddress(
				Integer.parseInt("9999"));
		// localAddress = address.getAddress().getHostAddress();
		accept.bind(address);
		logger.info("长连接服务器启动成功");
	}
}
