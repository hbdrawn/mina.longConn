package com.bkht.mina.heart;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//当心跳机制启动的时候，需要该工厂类来判断和定制心跳消息  
public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {
	private static final byte int_req = -1;

	private static final byte int_rep = -2;

	private final IoBuffer KAMSG_REQ = IoBuffer.wrap(new byte[] { int_req });

	private final IoBuffer KAMSG_REP = IoBuffer.wrap(new byte[] { int_rep });

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if (!(message instanceof IoBuffer))
			return false;
		IoBuffer realMessage = (IoBuffer) message;
		if (realMessage.limit() != 1)
			return false;

		boolean result = (realMessage.get() == int_req);
		realMessage.rewind();
		return result;

	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		if (!(message instanceof IoBuffer))
			return false;
		IoBuffer realMessage = (IoBuffer) message;
		if (realMessage.limit() != 1)
			return false;

		boolean result = (realMessage.get() == int_rep);
		realMessage.rewind();
		return result;
	}

	@Override
	public Object getRequest(IoSession session) {
		return KAMSG_REQ.duplicate();
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		// 将试用次数复原，心跳发送异常会更改次数
//		if (session.getAttribute("tryCount") != null) {
//			int count = (Integer) session.getAttribute("tryCount");
//			if (count < LongConnHandler.tryCount) {
//				session.setAttribute("tryCount", LongConnHandler.tryCount);
//			}
//		}
		return KAMSG_REP.duplicate();
	}

}

class MyKeepAliveFilter extends KeepAliveFilter {
	private static final int INTERVAL = 60; // in seconds

	private static final int TIMEOUT = 30; // in seconds

	MyKeepAliveFilter(KeepAliveMessageFactory messageFactory) {
		super(messageFactory, IdleStatus.BOTH_IDLE, new ExceptionHandler(),
				INTERVAL, TIMEOUT);
	}

	MyKeepAliveFilter() {
		super(new KeepAliveMessageFactoryImpl(), IdleStatus.BOTH_IDLE,
				new ExceptionHandler(), INTERVAL, TIMEOUT);
		this.setForwardEvent(false); // 心跳消息不会继续传播，不会被业务层看到
	}

}

// 心跳发送三次失败后关闭连接
class ExceptionHandler implements KeepAliveRequestTimeoutHandler {
	public Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void keepAliveRequestTimedOut(KeepAliveFilter filter,
			IoSession session) throws Exception {
		int count = (Integer) session.getAttribute("tryCount");
		if (count < 1) {
			session.close(true);
		}
		session.setAttribute("tryCount", --count);
		logger.warn(">>>长连接超时，正在重试[{}]", count);
	}

}