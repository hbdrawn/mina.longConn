package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.utils.StringTools;

// 设备注册
public class T0x0501 extends TradeInAbstract {

	@Override
	public void unpackBody() throws Exception {
		byte[] body = wrapper.getMsgBody();
		// 鉴权码
		int vCode = SocketMessage.getInt(body, 0);
		// 设备编号
		int deviceId = SocketMessage.getInt(body, 4);
		// 保留
		byte reserved = body[8];
		// CCID
		byte[] ttbytes = new byte[20];
		System.arraycopy(body, 9, ttbytes, 0, 20);
		String ccid = StringTools.getString(ttbytes);

		logger.debug(">>>鉴权码:{},设备编号:{},保留位:{},CCID:{}", vCode, deviceId,
				reserved, ccid);
	}

	public void response(IoSession session) throws Exception {
		M0x9501 res = new M0x9501();
		res.setMsgId(wrapper.getMsgId());
		res.setMsgSerial(wrapper.getMsgSerial());
		res.setResult((byte) 0x00);
		res.setTime(null);
		byte[] msgBody = res.getBody();
		BodyPros bodyPros = new BodyPros("0", "000", (short) msgBody.length);
		MsgWrapper resWrapper = new MsgWrapper("9501", bodyPros,
				wrapper.getCarId(), wrapper.getMsgSerial(), null, msgBody);
		byte[] abuffer = resWrapper.getBytes();
		SocketMessage sm = new SocketMessage(abuffer.length, abuffer);
		session.write(sm);
	}
}
