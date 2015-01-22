package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.msg.MsgWrapper;

public abstract class TradeAbstract {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected MsgWrapper wrapper;

	// 交易的抽象类
	public void doHandler(IoSession session, MsgWrapper wrapper)
			throws Exception {
		setWrapper(wrapper);
		logger.debug("报文对象：" + wrapper.toString());
		// 设置消息实体
		// setWrapper(wrapper);
		// 处理消息体
		unpackBody();
		// 返回消息给客户端
		response(session);
	};

	public void setWrapper(MsgWrapper wrapper) {
		this.wrapper = wrapper;
	}

	// 下面两个方法体被子类覆盖---------------------------
	public void response(IoSession session) throws Exception {
		logger.info("业务子类中无覆盖方法，默认不返回消息");
	}

	// 拆每笔交易的报文体
	public abstract void unpackBody() throws Exception;

}
