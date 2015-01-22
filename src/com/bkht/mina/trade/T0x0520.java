package com.bkht.mina.trade;

import com.bkht.mina.comm.SocketMessage;

// 终端通用应答
public class T0x0520 extends TradeAbstract {

	@Override
	public void unpackBody() throws Exception {
		byte[] body = wrapper.getMsgBody();
		// 应答流水号
		short resSerial = SocketMessage.getShort(body, 0);
		// 应答ID
		short resId = SocketMessage.getShort(body, 2);
		// 结果
		byte result = body[body.length - 1];
		logger.debug(">>>应答流水号:{}, 应答ID:{}, 结果:{}", resSerial, resId,
				(int) result);
	}
}
