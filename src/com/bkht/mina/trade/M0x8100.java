package com.bkht.mina.trade;

import com.bkht.mina.comm.SocketMessage;

public class M0x8100 {

	private short msgSeiral; // 应答流水号

	private byte result; // 结果 0：成功；1：车辆已被注册；2：数据库中无该车辆；3：终端已被注册；4：数据库中无该终端

	private int checkCode; // 鉴权码 4字节

	public byte[] getBody() throws Exception {
		byte[] buff = new byte[2 + 1 + 4];
		SocketMessage.setShort(buff, 0, msgSeiral);
		buff[2] = result;
		SocketMessage.setInt(buff, 3, checkCode);
		return buff;
	}

	public short getMsgSeiral() {
		return msgSeiral;
	}

	public void setMsgSeiral(short msgSeiral) {
		this.msgSeiral = msgSeiral;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public int getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(int checkCode) {
		this.checkCode = checkCode;
	}
}
