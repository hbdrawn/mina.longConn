package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;

// 设备注册
public class T0x0506 extends TradeAbstract {

	@Override
	public void unpackBody() throws Exception {
		logger.debug(">>>终端上送心跳报文");
	}

	public void response(IoSession session) throws Exception {
		M0x9520 res = new M0x9520();
		res.setMsgId(wrapper.getMsgId());
		res.setMsgSerial(wrapper.getMsgSerial());
		res.setResult((byte) 0x00);
		// 设置消息头属性
		byte[] msgBody = res.getBody();
		BodyPros bodyPros = new BodyPros("0", "000", (short) msgBody.length);
		MsgWrapper resWrapper = new MsgWrapper("9520", bodyPros,
				wrapper.getCarId(), wrapper.getMsgSerial(), null, msgBody);
		byte[] abuffer = resWrapper.getBytes();
		SocketMessage sm = new SocketMessage(abuffer.length, abuffer);
		session.write(sm);
	}
}
