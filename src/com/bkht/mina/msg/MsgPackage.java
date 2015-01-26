package com.bkht.mina.msg;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.StringTools;

// 消息包封装项 4字节
public class MsgPackage {

	public short sum; // 消息包总数 2字节
	public short serial; // 包序号 2字节

	public MsgPackage(byte[] buffer) {
		sum = SocketMessage.getShort(buffer, 0);
		serial = SocketMessage.getShort(buffer, 2);
	}

	public MsgPackage(short sum, short serial) {
		this.sum = sum;
		this.serial = serial;
	}

	public byte[] getBytes() {
		byte[] bytes = new byte[4];
		byte[] sumb = ByteTools.getShort(sum);
		byte[] serialb = ByteTools.getShort(serial);
		System.arraycopy(sumb, 0, bytes, 0, 2);
		System.arraycopy(serialb, 0, bytes, 2, 2);
		return bytes;
	}

	public String toString() {
		return "{sum:" + sum + ",serial:" + serial + "}";
	}

	public static void main(String[] args) {
		MsgPackage m = new MsgPackage((short) 101, (short) 1);
		System.out.println(StringTools.toHexTable(m.getBytes()));
	}

}
