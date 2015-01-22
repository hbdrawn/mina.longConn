package com.bkht.mina.msg;

import com.bkht.mina.utils.ByteTools;

//消息体属性，两个字节
public class BodyPros {

	public String reserved = "00"; // 保留 2bit
	public String packageNum; // 分包 1bit
	public String encrypt; // 数据加密方式，3bit
	public short bodyLen; // 消息体长度，10bit 故单包的最大长度为2^10-1=1021字节

	public BodyPros(byte[] abuffer) {
		String str = ByteTools.byteToBit(abuffer[0])
				+ ByteTools.byteToBit(abuffer[1]);
		reserved = str.substring(0, 2);
		packageNum = str.substring(2, 3);
		encrypt = str.substring(3, 6);
		bodyLen = Short.valueOf(str.substring(6, 16), 2);
	}

	public BodyPros(String packageNum, String encrypt, short bodyLen) {
		this.packageNum = packageNum;
		this.encrypt = encrypt;
		this.bodyLen = bodyLen;
	}

	public byte[] getBytes() throws Exception {
		if (bodyLen > MsgWrapper.MAX_BUFFER) {
			throw new Exception("消息体长度错误，最大为1021字节，当前为[" + bodyLen + "]");
		}
		// 预加2bit，带转换为byte后去除
		String prefix = reserved + packageNum + encrypt;
		// byte bitToByte = ByteTools.BitToByte(prefix);
		byte[] all = ByteTools.getShort(bodyLen);
		String str1 = ByteTools.byteToBit(all[0]);
		String str2 = ByteTools.byteToBit(all[1]);
		String str = str1 + str2;
		String bitStr = prefix + str.substring(6);
		byte[] buff = new byte[2];
		buff[0] = ByteTools.BitToByte(bitStr.substring(0, bitStr.length() / 2));
		buff[1] = ByteTools.BitToByte(bitStr.substring(bitStr.length() / 2));
		return buff;
	}

	public String toString() {
		return "{reserved:" + reserved + ",packageNum:" + packageNum
				+ ",encrypt:" + encrypt + ",bodyLen:" + bodyLen + "}";
	}

}
