package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.utils.ByteTools;

// 设备注册
public class T0x0502 extends TradeInAbstract {

	@Override
	public void unpackBody() throws Exception {
		byte[] body = wrapper.getMsgBody();
		// 进出标识
		byte vbyte = body[0];
		int v = (int) (vbyte & 0xff);
		// 设备编号
		byte[] identifyBytes = new byte[10];
		System.arraycopy(body, 1, identifyBytes, 0, identifyBytes.length);
		String identify = ByteTools.bytes2Identify(identifyBytes);
		logger.debug(">>>进出标识:{},身份证:{}", v, identify);
	}

	public void response(IoSession session) throws Exception {
		M0x9520 res = new M0x9520();
		res.setMsgId(wrapper.getMsgId());
		res.setMsgSerial(wrapper.getMsgSerial());
		res.setResult((byte) 0x00);
		byte[] msgBody = res.getBody();
		BodyPros bodyPros = new BodyPros("0", "000", (short) msgBody.length);
		MsgWrapper resWrapper = new MsgWrapper("9520", bodyPros,
				wrapper.getCarId(), wrapper.getMsgSerial(), null, msgBody);
		byte[] abuffer = resWrapper.getBytes();
		SocketMessage sm = new SocketMessage(abuffer.length, abuffer);
		session.write(sm);
	}
}
