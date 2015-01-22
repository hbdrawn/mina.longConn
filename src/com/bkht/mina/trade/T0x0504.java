package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.StringTools;
import com.bkht.mina.utils.TimeUtils;

// 消息位置上传
public class T0x0504 extends TradeAbstract {

	@Override
	public void unpackBody() throws Exception {
		byte[] body = wrapper.getMsgBody();
		// 报警标识
		byte[] alarmIdBytes = new byte[4];
		System.arraycopy(body, 0, alarmIdBytes, 0, 4);
		String alarmId = ByteTools.bytesToBit(alarmIdBytes);
		// 状态
		byte[] statusBytes = new byte[4];
		System.arraycopy(body, 4, statusBytes, 0, 4);
		String status = ByteTools.bytesToBit(statusBytes);
		// 纬度
		int latitude = SocketMessage.getInt(body, 8);
		// 经度
		int longitude = SocketMessage.getInt(body, 12);
		// 高程
		short elevation = SocketMessage.getShort(body, 16);
		// 速度
		short speed = SocketMessage.getShort(body, 18);
		// 方向
		short direction = SocketMessage.getShort(body, 20);
		// 时间
		byte[] timeBytes = new byte[6];
		System.arraycopy(body, 22, timeBytes, 0, 6);
		String time = TimeUtils.setTime(timeBytes);
		// 剩余续航里程
		int surplusDistance = SocketMessage.getInt(body, 28);
		// 总里程
		int distance = SocketMessage.getInt(body, 32);
		// 当前车辆电压,单位时0.1v
		short voltage = SocketMessage.getShort(body, 36);
		// reserved
		byte[] reservedBytes = new byte[8];
		System.arraycopy(body, 38, reservedBytes, 0, 8);
		String reserved = StringTools.getString(reservedBytes);

		logger.debug(
				">>>报警标识:{},状态:{},纬度:{},经度:{},高程:{},速度:{},方向:{},时间:{},剩余续航里程:{},总里程:{},当前车辆电压:{},预留:{}",
				alarmId, status, latitude, longitude, elevation, speed,
				direction, time, surplusDistance, distance, voltage, reserved);
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
