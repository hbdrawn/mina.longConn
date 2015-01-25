package com.bkht.mina.trade;

//终端升级结果通知
public class T0x0108 extends TradeInAbstract {

	@Override
	public void unpackBody() throws Exception {
		byte[] buff = wrapper.getMsgBody();
		// 升级类型0：终端，12：道路运输证IC 卡读卡器，52：北斗卫星定位模块
		byte type = buff[0];
		int typeInt = (int) (type & 0xff);
		// 升级结果
		byte result = buff[1];
		int resultInt = (int) (result & 0xff);
		logger.info("升级类型:{}, 升级结果:{}", typeInt, resultInt);
	}
}
