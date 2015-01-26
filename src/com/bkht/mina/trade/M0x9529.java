package com.bkht.mina.trade;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.StringTools;

//设置终端参数
public class M0x9529 implements MsgAbstract {

	private Integer x0001 = null;// 终端心跳发送间隔，单位为秒（s）
	private String x0013 = null;// 主服务器地址,IP
	private String x0017 = null;// 备份服务器地址,IP
	private Integer x0018 = null; // 服务器TCP 端口
	private Integer x0027 = null; // 休眠时汇报时间间隔，单位为秒（s），>0
	private Integer x0029 = null; // 工作时时间汇报间隔，单位为秒（s），>0
	private Integer x002c = null; // 工作时距离汇报间隔，单位为米（m），>0
	private Integer x002e = null; // 休眠时汇报距离间隔，单位为米（m），>0

	public static final byte X0001 = 0x0001;
	public static final byte X0013 = 0x0013;
	public static final byte X0017 = 0x0017;
	public static final byte X0018 = 0x0018;
	public static final byte X0027 = 0x0027;
	public static final byte X0029 = 0x0029;
	public static final byte X002c = 0x002C;
	public static final byte X002e = 0x002E;

	@Override
	public byte[] getBody() throws Exception {
		int sum = 0;
		int len = 1;
		byte[] result = new byte[30];
		if (x0001 != null) {
			sum++;
			result[len] = X0001;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x0001);
			len = len + 6;
		}
		if (x0013 != null) {
			sum++;
			result[len] = X0013;
			byte[] by = x0013.getBytes();
			result[len + 1] = (byte) by.length;
			System.arraycopy(by, 0, result, len + 2, by.length);
			len = len + by.length + 2;
		}
		if (x0017 != null) {
			sum++;
			result[len] = X0017;
			byte[] by = x0017.getBytes();
			result[len + 1] = (byte) by.length;
			System.arraycopy(by, 0, result, len + 2, by.length);
			len = len + by.length + 2;
		}
		if (x0018 != null) {
			sum++;
			result[len] = X0018;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x0018);
			len = len + 6;
		}
		if (x0027 != null) {
			sum++;
			result[len] = X0027;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x0027);
			len = len + 6;
		}
		if (x0029 != null) {
			sum++;
			result[len] = X0029;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x0029);
			len = len + 6;
		}
		if (x002c != null) {
			sum++;
			result[len] = X002c;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x002c);
			len = len + 6;
		}
		if (x002e != null) {
			sum++;
			result[len] = X002e;
			result[len + 1] = (byte) 4;
			SocketMessage.setInt(result, len + 2, x002e);
			len = len + 6;
		}

		if (sum == 0) {
			return null;
		} else {
			result[0] = (byte) sum;
		}

		byte[] buffer = new byte[len];
		System.arraycopy(result, 0, buffer, 0, len);
		return buffer;
	}

	public Integer getX0001() {
		return x0001;
	}

	public void setX0001(Integer x0001) {
		this.x0001 = x0001;
	}

	public String getX0013() {
		return x0013;
	}

	public void setX0013(String x0013) {
		this.x0013 = x0013;
	}

	public String getX0017() {
		return x0017;
	}

	public void setX0017(String x0017) {
		this.x0017 = x0017;
	}

	public Integer getX0018() {
		return x0018;
	}

	public void setX0018(Integer x0018) {
		this.x0018 = x0018;
	}

	public Integer getX0027() {
		return x0027;
	}

	public void setX0027(Integer x0027) {
		this.x0027 = x0027;
	}

	public Integer getX0029() {
		return x0029;
	}

	public void setX0029(Integer x0029) {
		this.x0029 = x0029;
	}

	public Integer getX002c() {
		return x002c;
	}

	public void setX002c(Integer x002c) {
		this.x002c = x002c;
	}

	public Integer getX002e() {
		return x002e;
	}

	public void setX002e(Integer x002e) {
		this.x002e = x002e;
	}

	public static void main(String[] args) throws Exception {
		M0x9529 m = new M0x9529();
		m.setX0001(4);
		m.setX002e(1);
		System.out.println(StringTools.toHexTable(m.getBody()));
	}

}
