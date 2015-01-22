package com.bkht.mina.trade;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.StringTools;

public class M0x9520 {

	private short msgSerial; // 应答流水号
	private String msgId; // 对应终端消息的ID 2bytes
	private byte result; // 结果 0：成功/确认；1：失败；2：消息有误

	public byte[] getBody() throws Exception {
		byte[] buff = new byte[5];
		SocketMessage.setShort(buff, 0, msgSerial);
		byte[] msgIdBytes = StringTools.string2Byte(msgId);
		System.arraycopy(msgIdBytes, 0, buff, 2, 2);
		buff[4] = result;
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

}
