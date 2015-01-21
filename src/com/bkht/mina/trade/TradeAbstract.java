package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.SocketMessageWrapper;

public abstract class TradeAbstract {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected SocketMessageWrapper wrapper;

	public TradeAbstract(SocketMessageWrapper wrapper) {
		this.wrapper = wrapper;
		logger.debug("报文对象：" + wrapper.toString());
	}

	// 交易的抽象类
	public abstract void doHandler(IoSession session);

	public void setWrapper(SocketMessageWrapper wrapper) {
		this.wrapper = wrapper;
	};
}
