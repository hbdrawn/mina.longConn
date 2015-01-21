package com.bkht.mina.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.SessionWrap;
import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.comm.SocketMessageWrapper;
import com.bkht.mina.trade.SysConstant;
import com.bkht.mina.trade.T256;
import com.bkht.mina.trade.TradeAbstract;

public class VehicleSocketHandler extends IoHandlerAdapter {
	private static Logger log = LoggerFactory
			.getLogger(VehicleSocketHandler.class);

	/**
	 * 功能说明：有异常发生时被触发
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error("vehicle socket execption", cause);
	}

	/**
	 * 功能说明：有消息到达时被触发，message代表接收到的消息
	 */
	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		SocketMessage msg = (SocketMessage) obj;
		SocketMessageWrapper wrapper = new SocketMessageWrapper(msg.getBytes());
		short msgId = wrapper.getMsgId();
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

		TradeAbstract trade = null;
		if (msgId == SysConstant.tag_0x0100) {
			trade = new T256(wrapper);
		} else {
			throw new Exception("交易码错误：" + msgId);
		}

		trade.doHandler(session);
		// if (tag == SysConstant.tag_v_test) {
		// // 客户端测试
		// int id = msg.getID();
		// SocketMessage returnMsg = new SocketMessage();
		// returnMsg.addByte(SysConstant.tag_v_test_reply);
		// returnMsg.addInt(id);
		// returnMsg.addInt(carid);
		// returnMsg.addShort((short) 10000);
		// returnMsg.addTime(System.currentTimeMillis());
		// returnMsg.addDouble(114.12345);
		// returnMsg.addRfid("4D66C6C6");
		// // 发送测试数据
		// SocketSender.sendToVehicle(returnMsg);
		// return;
		// }

		// 转发给服务器
		// if (msg.getTag() <= 100)
		// SocketSender.sendToServer(msg);
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
		log.info("vehicle session closed");
		super.sessionClosed(session);
	}

	/**
	 * 功能说明：当创建一个新连接时被触发，即当开始一个新的Session时被触发。
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.info("vehicle session created");
		super.sessionCreated(session);
	}

	/**
	 * 功能说明：当打开一个连接时被触发
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.info("vehicle session opened");
		super.sessionOpened(session);
	}

}
