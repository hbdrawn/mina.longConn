package com.bkht.mina.comm;

import com.bkht.mina.utils.ByteTools;

//消息体封装
public class SocketMessageWrapper {

	private short msgId; // 消息ID; 2 byte

	private BodyPros bodyPros; // 消息体属性; 2 byte

	private String carId; // 车辆管理ID; 四个字节，后两个字节补0

	private short msgSerial; // 消息流水号，按发送顺序从0开始递增

	private MsgPackage msgPackage; //

	private byte[] msgBody; // 消息体

	public static final int MAX_BUFFER = 1021;
	public static final short sign = (short) 61568; // F0 80

	public SocketMessageWrapper() {
	}

	public SocketMessageWrapper(byte[] abuffer) throws Exception {
		setMsgId(SocketMessage.getShort(abuffer, 0));
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
			msgBody = new byte[abuffer.length - 12];
			System.arraycopy(abuffer, 12, msgBody, 0, abuffer.length - 12 - 1);
		}
	}

	public byte[] getBytes() throws Exception {
		if (msgBody == null || bodyPros == null || carId == null) {
			throw new Exception("参数不合法");
		}
		byte[] msgIdBytes = ByteTools.getShort(msgId);
		byte[] bodyProsBytes = bodyPros.getBytes();
		byte[] carIdBytes = ByteTools.asc2Bcd(carId.getBytes());
		if (carIdBytes.length != 6) {
			throw new Exception("参数不合法:车辆管理ID");
		}
		byte[] msgSerialBytes = ByteTools.getShort(msgSerial);
		byte[] msgPackageBytes = null;

		int len = 0;
		if (msgPackage != null) {
			len = 16 + msgBody.length;
			msgPackageBytes = msgPackage.getBytes();
		} else {
			len = 12 + msgBody.length;
		}
		byte[] msg = new byte[len];
		System.arraycopy(msgIdBytes, 0, msg, 0, 2);
		System.arraycopy(bodyProsBytes, 0, msg, 2, 2);
		System.arraycopy(carIdBytes, 0, msg, 4, 6);
		System.arraycopy(msgSerialBytes, 0, msg, 10, 2);
		if (msgPackageBytes != null) {
			System.arraycopy(msgPackageBytes, 0, msg, 12, 2);
			System.arraycopy(msgBody, 0, msg, 16, msgBody.length);
		} else {
			System.arraycopy(msgBody, 0, msg, 12, msgBody.length);
		}

		return null;
	}

	public short getMsgId() {
		return msgId;
	}

	public void setMsgId(short msgId) {
		this.msgId = msgId;
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

	// 消息体属性，两个字节
	class BodyPros {
		public String reserved = "00"; // 保留 2bit
		public String packageNum; // 分包 1bit
		public String encrypt; // 数据加密方式，3bit
		public short bodyLen; // 消息体长度，10bit 故单包的最大长度为2^10-1=1021字节

		BodyPros(byte[] abuffer) {
			String str = ByteTools.byteToBit(abuffer[0])
					+ ByteTools.byteToBit(abuffer[1]);
			reserved = str.substring(0, 2);
			packageNum = str.substring(2, 3);
			encrypt = str.substring(3, 6);
			bodyLen = Short.valueOf(str.substring(6, 16), 2);
		}

		BodyPros(String packageNum, String encrypt, short bodyLen) {
			this.packageNum = packageNum;
			this.encrypt = encrypt;
			this.bodyLen = bodyLen;
		}

		public byte[] getBytes() throws Exception {
			if (bodyLen > MAX_BUFFER) {
				throw new Exception("消息体长度错误，最大为1021字节，当前为[" + bodyLen + "]");
			}
			// 预加2bit，带转换为byte后去除
			String prefix = reserved + packageNum + encrypt + "00";
			byte bitToByte = ByteTools.BitToByte(prefix);
			byte[] all = ByteTools.getShort(bodyLen);
			System.arraycopy(bitToByte, 0, all, 0, 6);
			return all;
		}

		public String toString() {
			return "{reserved:" + reserved + ",packageNum:" + packageNum
					+ ",encrypt:" + encrypt + ",bodyLen" + bodyLen + "}";
		}
	}

	// 消息包封装项 4字节
	class MsgPackage {
		public short sum; // 消息包总数 2字节
		public short serial; // 包序号 2字节

		MsgPackage(byte[] buffer) {
			sum = SocketMessage.getShort(buffer, 0);
			serial = SocketMessage.getShort(buffer, 2);
		}

		MsgPackage(short sum, short serial) {
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
			if (sum == 0 || serial == 0) {
				return null;
			} else {
				return "{sum:" + sum + ",serial:" + serial + "}";
			}
		}
	}

	public String toString() {
		return "{msgId:" + msgId + "bodyPros:" + bodyPros.toString()
				+ ",carId:" + carId + ",msgSerial" + msgSerial + ",msgPackage:"
				+ msgPackage.toString() + ",msgBody:" + new String(msgBody)
				+ "}";
	}
}
