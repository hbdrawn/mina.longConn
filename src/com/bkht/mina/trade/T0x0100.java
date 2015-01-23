package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.StringTools;

// 设备注册
public class T0x0100 extends TradeInAbstract {

	@Override
	public void unpackBody() throws Exception{
		byte[] body = wrapper.getMsgBody();
		// 省域ID
		short provinceId = SocketMessage.getShort(body, 0);
		// 市县域ID
		short cityId = SocketMessage.getShort(body, 2);
		// 制造商ID
		byte[] mbytes = new byte[5];
		System.arraycopy(body, 4, mbytes, 0, 5);
		String manufacturer = StringTools.getString(mbytes);
		// 终端型号ID
		byte[] ttbytes = new byte[20];
		System.arraycopy(body, 9, ttbytes, 0, 20);
		String terminalType = StringTools.getString(ttbytes);
		// 终端ID
		byte[] tbytes = new byte[7];
		System.arraycopy(body, 29, tbytes, 0, 7);
		String terminalId = StringTools.getString(ByteTools.bcd2Asc(tbytes,
				false));
		// 车牌颜色
		byte color = body[36];
		// 车辆标识
		byte[] cbytes = new byte[body.length - 36 - 1];
		System.arraycopy(body, 37, cbytes, 0, cbytes.length);
		String carId = StringTools.getString(cbytes);
		logger.debug(
				">>>省域ID:{},县市ID:{},制造商ID:{},终端型号ID:{},终端ID:{},车牌颜色:{},车辆标识:{}",
				provinceId, cityId, manufacturer, terminalType, terminalId,
				color, carId);
	}

	public void response(IoSession session) throws Exception {
		M0x8100 res = new M0x8100();
		res.setMsgSeiral(wrapper.getMsgSerial());
		res.setResult((byte) 0x00);
		// 鉴权码如何获得TODO
		res.setCheckCode(1);
		// 设置消息头属性
		byte[] msgBody = res.getBody();
		BodyPros bodyPros = new BodyPros("0", "000", (short) msgBody.length);
		MsgWrapper resWrapper = new MsgWrapper("8100", bodyPros,
				wrapper.getCarId(), wrapper.getMsgSerial(), null, msgBody);
		byte[] abuffer = resWrapper.getBytes();
		SocketMessage sm = new SocketMessage(abuffer.length, abuffer);
		session.write(sm);
	}
}
