package com.bkht.mina.trade;

import java.util.Date;

import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.TimeUtils;

//租车订单下发
public class M0x9521 implements MsgAbstract {

	private Date stime; // 起租时间
	private Date etime;// 截止时间
	private String identify;// 身份证号码
	private byte time; // 允许拖延时间，单位分钟 0 - 240

	@Override
	public byte[] getBody() throws Exception {
		byte[] stimeBytes = getStime();
		byte[] etimeBytes = getEtime();
		byte[] idBytes = ByteTools.identify2Bytes(identify);
		byte[] buffer = new byte[stimeBytes.length + etimeBytes.length
				+ idBytes.length + 1];
		System.arraycopy(stimeBytes, 0, buffer, 0, stimeBytes.length);
		System.arraycopy(etimeBytes, 0, buffer, stimeBytes.length,
				etimeBytes.length);
		System.arraycopy(idBytes, 0, buffer, stimeBytes.length
				+ etimeBytes.length, idBytes.length);
		buffer[buffer.length - 1] = time;
		return buffer;
	}

	public byte[] getStime() throws Exception {
		return TimeUtils.getTime(stime);
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public byte[] getEtime() throws Exception {
		return TimeUtils.getTime(etime);
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public byte getTime() {
		return time;
	}

	public void setTime(byte time) {
		this.time = time;
	}

}
