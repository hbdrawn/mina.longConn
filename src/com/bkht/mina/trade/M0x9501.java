package com.bkht.mina.trade;

import java.util.Date;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.StringTools;
import com.bkht.mina.utils.TimeUtils;

public class M0x9501 implements MsgAbstract {

	private short msgSerial; // 应答流水号
	private String msgId; // 对应终端消息的ID 2bytes
	private byte result; // 结果 0：成功/确认；1：失败；2：消息有误
	private Date time; // 服务器时间，bcd[6],YY-MM-DD-hh-mm-ss

	public byte[] getBody() throws Exception {
		byte[] buff = new byte[11];
		SocketMessage.setShort(buff, 0, msgSerial);
		byte[] msgIdBytes = StringTools.string2Byte(msgId);
		System.arraycopy(msgIdBytes, 0, buff, 2, 2);
		buff[4] = result;
		byte[] bcd = getTime();
		System.arraycopy(bcd, 0, buff, 5, 6);
		return buff;
	}

	public short getMsgSerial() {
		return msgSerial;
	}

	public void setMsgSerial(short msgSerial) {
		this.msgSerial = msgSerial;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public byte[] getTime() throws Exception {
		return TimeUtils.getTime(time);
	}

	public void setTime(Date time) {
		this.time = time;
	}

	// public static void main(String[] args) throws Exception {
	// M0x9501 i = new M0x9501();
	// i.setMsgId("0x0100");
	// i.setMsgSerial((short) 3);
	// i.setResult((byte) 0x00);
	// // i.setTime(new Date().toString());
	// System.out.println(StringTools.toHexString(i.getBody()));
	// }

}
