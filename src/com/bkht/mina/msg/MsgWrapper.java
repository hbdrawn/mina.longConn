package com.bkht.mina.msg;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.StringTools;

//消息体封装
public class MsgWrapper {

	private String msgId; // 消息ID; 2 byte,unsighn short,这里用stringhex表示

	private BodyPros bodyPros; // 消息体属性; 2 byte

	private String carId; // 车辆管理ID; 四个字节，后两个字节补0

	private short msgSerial; // 消息流水号，按发送顺序从0开始递增

	private MsgPackage msgPackage; //

	private byte[] msgBody; // 消息体

	public static final int MAX_BUFFER = 1023; // 最大消息体长度

	public static final int MAX_BUFFER_MSG = MAX_BUFFER + 14 + 1 + 2; // 最大消息体长度

	// public static final String HEX_PREFIX = "0x";
	public static final short sign = (short) 61568; // F0 80

	public MsgWrapper() {
	}

	public MsgWrapper(String msgId, BodyPros bodyPros, String carId,
			short msgSerial, MsgPackage msgPackage, byte[] msgBody) {
		this.msgId = msgId;
		this.bodyPros = bodyPros;
		this.carId = carId;
		this.msgSerial = msgSerial;
		this.msgPackage = msgPackage;
		this.msgBody = msgBody;
	}

	public MsgWrapper(byte[] abuffer) throws Exception {
		msgId = StringTools.getMsgIdFromBytes(abuffer, 0);
		bodyPros = new BodyPros(new byte[] { abuffer[2], abuffer[3] });
		byte[] carId_tmp = new byte[6];
		System.arraycopy(abuffer, 4, carId_tmp, 0, 6);
		// TODO 确认编码
		carId = new String(ByteTools.bcd2Asc(carId_tmp, true));
		msgSerial = SocketMessage.getShort(abuffer, 10);
		if (bodyPros.packageNum.equals("1")) {
			byte[] msgPackageBytes = new byte[4];
			System.arraycopy(abuffer, 12, msgPackageBytes, 0, 4);
			msgPackage = new MsgPackage(msgPackageBytes);
			msgBody = new byte[abuffer.length - 16];
			// 减一是对校验码处理
			System.arraycopy(abuffer, 16, msgBody, 0, abuffer.length - 16 - 1);
		} else {
			// 处理消息体是否为空
			if (bodyPros.bodyLen != 0) {
				msgBody = new byte[abuffer.length - 12];
				System.arraycopy(abuffer, 12, msgBody, 0, abuffer.length - 12);
			}
		}
	}

	public byte[] getBytes() throws Exception {
		if (bodyPros == null || carId == null) {
			throw new Exception("参数不合法");
		}
		byte[] msgIdBytes = setMsgId(msgId);
		byte[] bodyProsBytes = bodyPros.getBytes();
		byte[] carIdBytes = ByteTools.asc2Bcd(carId.getBytes());
		if (carIdBytes.length != 6) {
			throw new Exception("参数不合法:车辆管理ID");
		}
		byte[] msgSerialBytes = ByteTools.getShort(msgSerial);
		byte[] msgPackageBytes = null;

		// 处理body为空的情况
		int msgBodyLen = msgBody == null ? 0 : msgBody.length;
		int len = 0;
		if (msgPackage != null) {
			len = 16 + msgBodyLen;
			msgPackageBytes = msgPackage.getBytes();
		} else {
			len = 12 + msgBodyLen;
		}
		byte[] msg = new byte[len];
		System.arraycopy(msgIdBytes, 0, msg, 0, 2);
		System.arraycopy(bodyProsBytes, 0, msg, 2, 2);
		System.arraycopy(carIdBytes, 0, msg, 4, 6);
		System.arraycopy(msgSerialBytes, 0, msg, 10, 2);
		if (msgPackageBytes != null) {
			System.arraycopy(msgPackageBytes, 0, msg, 12, 4);
			if (msgBodyLen != 0) {
				System.arraycopy(msgBody, 0, msg, 16, msgBody.length);
			}
		} else {
			if (msgBodyLen != 0) {
				System.arraycopy(msgBody, 0, msg, 12, msgBody.length);
			}
		}

		return msg;
	}

	// msgId是bytes<=>hexstring
	public String getMsgId() {
		return msgId;
	}

	public byte[] setMsgId(String hex) throws Exception {
		return StringTools.string2Byte(hex);
	}

	public BodyPros getBodyPros() {
		return bodyPros;
	}

	public void setBodyPros(BodyPros bodyPros) {
		this.bodyPros = bodyPros;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public short getMsgSerial() {
		return msgSerial;
	}

	public void setMsgSerial(short msgSerial) {
		this.msgSerial = msgSerial;
	}

	public MsgPackage getMsgPackage() {
		return msgPackage;
	}

	public void setMsgPackage(MsgPackage msgPackage) {
		this.msgPackage = msgPackage;
	}

	public byte[] getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(byte[] msgBody) {
		this.msgBody = msgBody;
	}

	public String toString() {
		return "{msgId:" + msgId + ",bodyPros:" + bodyPros.toString()
				+ ",carId:" + carId + ",msgSerial:" + msgSerial
				+ ",msgPackage:"
				+ (msgPackage == null ? null : msgPackage.toString())
				+ ",msgBody:"
				+ (msgBody == null ? null : StringTools.toHexString(msgBody))
				+ "}";
	}

	// public static void main(String[] args) throws Exception {
	// String msg = "05200005131013240000A0001122952102";
	// byte[] string2Byte = StringTools.string2Byte(msg);
	// MsgWrapper w = new MsgWrapper(string2Byte);
	// System.out.println(StringTools.toHexTable(w.getMsgBody()));
	// System.out.println(w.toString());
	// }

}
