package com.bkht.mina.server;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.trade.M0x9521;
import com.bkht.mina.trade.M0x9561;
import com.bkht.mina.trade.TradeOut;

public class SendToVetical {

	public static Logger logger = LoggerFactory.getLogger(SendToVetical.class);

	static String carId = "131013240000";

	// 根据校验码发送不同报文到终端
	public static void send2Vetical(String msg) throws Exception {
		MsgWrapper wrapper = null;
		logger.debug("交易码：{}", msg);
		if (msg.equals("0x9521")) {
			// 租车订单下发
			M0x9521 t0x9521 = new M0x9521();
			Calendar calendar = Calendar.getInstance();
			t0x9521.setStime(calendar.getTime());
			calendar.add(Calendar.HOUR, 3);
			t0x9521.setEtime(calendar.getTime());
			t0x9521.setIdentify("19809878392098983A");
			t0x9521.setTime((byte) 200);
			wrapper = new MsgWrapper("9521", new BodyPros("0", "000",
					(short) t0x9521.getBody().length), carId, (short) 1, null,
					t0x9521.getBody());
		} else if (msg.equals("0x9560")) {
			// 询问终端状态，消息体为空
			wrapper = new MsgWrapper("9560",
					new BodyPros("0", "000", (short) 0), carId, (short) 1,
					null, null);
		} else if (msg.equals("0x9561")) {
			// 服务器删除终端订单
			M0x9561 m0x9561 = new M0x9561();
			m0x9561.setIdentify("198098783920989834");
			wrapper = new MsgWrapper("9561", new BodyPros("0", "000",
					(short) m0x9561.getBody().length), carId, (short) 1, null,
					m0x9561.getBody());
		} else if (msg.equals("0x9562")) {
			// 服务器下发找车
			byte[] biaozhi = new byte[1];
			biaozhi[0] = 0x02;
			wrapper = new MsgWrapper("9562", new BodyPros("0", "000",
					(short) biaozhi.length), carId, (short) 1, null, biaozhi);
		} else if (msg.equals("0x9563")) {
			// 服务器下发强制开门/关门
			byte[] biaozhi = new byte[1];
			biaozhi[0] = 0x01;
			wrapper = new MsgWrapper("9563", new BodyPros("0", "000",
					(short) biaozhi.length), carId, (short) 1, null, biaozhi);
		} else if (msg.equals("0x9529")) {
			// 设置终端参数
			// TODO
		} else if (msg.equals("0x8107")) {
			// 查询终端参数,消息体为空
			wrapper = new MsgWrapper("8107",
					new BodyPros("0", "000", (short) 0), carId, (short) 1,
					null, null);
		} else if (msg.equals("0x8108")) {
			// 下发终端升级包

		} else {
			throw new Exception("交易码错误，请重试:" + msg);
		}

		if (wrapper != null) {
			TradeOut trade = new TradeOut(wrapper);
			trade.doHandler(VehicleSessionHolder.getSession(carId));
		} else {
			logger.warn("====没有消息需要发送");
		}
	}

}
