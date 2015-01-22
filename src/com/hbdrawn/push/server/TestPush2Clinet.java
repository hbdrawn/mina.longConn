package com.hbdrawn.push.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.server.VehicleSessionHolder;

//import com.hbdrawn.server.ApplicationStartService;

public class TestPush2Clinet extends IoHandlerAdapter {

	public Logger logger = LoggerFactory.getLogger(getClass());

	public static IoAcceptor accept = null;

	// public static String localAddress = null;

	public void start() throws IOException {
		accept = new NioSocketAcceptor();
		accept.setHandler(new TestPush2Clinet());
		DefaultIoFilterChainBuilder chain = accept.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		// 心跳
		// chain.addLast("keep-alive", new MyKeepAliveFilter());
		chain.addLast("codec", new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));

		SocketSessionConfig config = (SocketSessionConfig) accept
				.getSessionConfig();
		config.setReadBufferSize(1024);
		config.setReuseAddress(true);
		config.setUseReadOperation(true);
		InetSocketAddress address = new InetSocketAddress(
				Integer.parseInt("8888"));
		// localAddress = address.getAddress().getHostAddress();
		accept.bind(address);
		logger.info("服务端测试服务启动");
	}

	@SuppressWarnings("unused")
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String msg = message.toString();
		// 订单下发
		MsgWrapper wrapper = null;
		if (msg.equals("0x9521")) {

		} else if (msg.equals("0x9560")) {
			// 询问终端状态
		} else if (msg.equals("0x9561")) {
			// 服务器删除终端订单
		} else if (msg.equals("0x9562")) {
			// 服务器下发找车
		} else if (msg.equals("0x9529")) {
			// 设置终端参数
		} else if (msg.equals("0x8107")) {
			// 查询终端参数
		} else if (msg.equals("0x8108")) {
			// 下发终端升级包
		} else {
			throw new Exception("交易码错误，请重试:" + msg);
		}

		if (wrapper != null) {
			VehicleSessionHolder.getSession(wrapper.getCarId()).send(
					new SocketMessage(wrapper.getBytes().length, wrapper
							.getBytes()));
		}
	}

	public void messageSent(IoSession session, Object message) throws Exception {

	}
}
