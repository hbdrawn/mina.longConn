package com.bkht.mina.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.SessionWrap;
import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.trade.TradeInAbstract;

public class VehicleSocketHandler extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory
			.getLogger(VehicleSocketHandler.class);

	/**
	 * 功能说明：有异常发生时被触发
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO
		// logger.error("vehicle socket execption", cause);
	}

	/**
	 * 功能说明：有消息到达时被触发，message代表接收到的消息
	 */
	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		SocketMessage msg = (SocketMessage) obj;
		MsgWrapper wrapper = new MsgWrapper(msg.getBytes());
		// 对消息体长度进行验证
		if (wrapper.getBodyPros().bodyLen == 0) {
			logger.info("报文体内容为空");
		} else if (wrapper.getBodyPros().bodyLen != wrapper.getMsgBody().length) {
			throw new Exception("报文头中报文体长度与报文体实际长度不符[bodyLen="
					+ wrapper.getBodyPros().bodyLen + ",body.len="
					+ wrapper.getMsgBody().length + "]");
		}
		String msgId = wrapper.getMsgId();
		String carId = wrapper.getCarId();
		SessionWrap sessionWrap = VehicleSessionHolder.getSession("" + carId);
		if (sessionWrap == null) {
			// 车辆连接,登记车辆
			sessionWrap = new SessionWrap(carId, session);
			VehicleSessionHolder.putSession(sessionWrap);
		} else {
			if (sessionWrap.getSession() != session) {
				sessionWrap = new SessionWrap(carId, session);
				VehicleSessionHolder.putSession(sessionWrap);
			}
		}
		sessionWrap.setLastActiveTime(System.currentTimeMillis());

		logger.info("交易码:{}", msgId);
		Class<?> class1 = null;
		try {
			class1 = Class.forName("com.bkht.mina.trade.T0x" + msgId);
		} catch (ClassNotFoundException ex) {
			throw new Exception("交易码错误或交易不存在:" + msgId);
		}
		TradeInAbstract trade = (TradeInAbstract) class1.newInstance();

		trade.doHandler(session, wrapper);
	}

	/**
	 * 功能说明：有消息到达时被触发，message代表接收到的消息
	 */
	@Override
	public void messageSent(IoSession session, Object json) throws Exception {
		super.messageSent(session, json);
	}

	/**
	 * 功能说明：当连接关闭时被触发，即Session终止时被触发。
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("vehicle session closed");
		super.sessionClosed(session);
	}

	/**
	 * 功能说明：当创建一个新连接时被触发，即当开始一个新的Session时被触发。
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("vehicle session created");
		super.sessionCreated(session);
	}

	/**
	 * 功能说明：当打开一个连接时被触发
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("vehicle session opened");
		super.sessionOpened(session);
	}

}
