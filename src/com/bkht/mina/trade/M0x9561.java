package com.bkht.mina.trade;

import com.bkht.mina.utils.ByteTools;

//询问终端状态
public class M0x9561 implements MsgAbstract {

	private String identify;// 身份证号码

	@Override
	public byte[] getBody() throws Exception {
		return getIdentify();
	}

	public byte[] getIdentify() throws Exception {
		return ByteTools.identify2Bytes(identify);
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}
}
