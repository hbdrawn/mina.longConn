package com.bkht.mina.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.comm.SessionWrap;
import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.msg.MsgWrapper;

//服务端发起的交易
public class TradeOut {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private MsgWrapper wrapper;

	public TradeOut(MsgWrapper wrapper) {
		this.wrapper = wrapper;
	}

	// private MsgAbstract msgBody;

	// 交易的抽象类
	public void doHandler(SessionWrap session) throws Exception {
		// 拼包
		// byte[] buffer = packBody();
		if (session == null) {
			throw new Exception("发送队列为空，尚未有终端建立连接");
		}
		SocketMessage sm = new SocketMessage(wrapper.getBytes().length,
				wrapper.getBytes());
		session.send(sm);
	};

	// // 拼报文体
	// public byte[] packBody() throws Exception {
	//
	// return null;
	// }

}
