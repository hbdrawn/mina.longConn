package com.hbdrawn.push.server;

import java.net.SocketException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LongConnHandler extends IoHandlerAdapter {
	public Logger logger = LoggerFactory.getLogger(getClass());

	// 重试次数
	public static final int tryCount = 3;

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// 设置重试次数
		session.setAttribute("tryCount", tryCount);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// session关闭时，需要从集群共享内存中删除
		ConnUtils.closeSession(session);
	}

	// 出现异常时，最大尝试次数为3
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		if (cause instanceof SocketException) {
			session.close(true);
		} else if (session.getAttribute("tryCount") != null) {
			int count = (Integer) session.getAttribute("tryCount");
			logger.warn(">>>长连接异常，正在重试[{}]\n{}", count, cause);
			if (count < 1) {
				session.close(true);
			} else {
				session.setAttribute("tryCount", --count);
			}
		}
	}

	// 心跳不會到handler层
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// 将试用次数复原，心跳发送异常会更改次数
		if (session.getAttribute("tryCount") != null) {
			int count = (Integer) session.getAttribute("tryCount");
			if (count < tryCount) {
				session.setAttribute("tryCount", tryCount);
			}
		}
		logger.info("{}接受数据{}", session.getRemoteAddress(), message.toString());
		// 位置信息处理
		String msg = message.toString();

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		logger.info("服务端向客户端[{}]发送数据[{}]", session.getRemoteAddress(), message);
	}
}