package com.bkht.mina.comm;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionWrap {
	private static Logger log = LoggerFactory.getLogger(SessionWrap.class);

	// 用来保存会话属性和发送消息；
	// 可以理解为服务器与客户端的特定连接，该连接由服务器地址、端口以及客户端地址、端口来决定。
	// 客户端发起请求时，指定服务器地址和端口，客户端也会指定或者根据网络路由信息自动指定一个地址、自动分配一个端口。
	// 这个地址、端口对构成一个Session。
	private IoSession session;
	private String carId;
	private long lastActiveTime = 0L;

	/**
	 * 获取一个会话对象
	 */
	public SessionWrap(String carid, IoSession session) {
		this.session = session;
		this.carId = carid;
	}

	/**
	 * 是否关闭缓存
	 * 
	 * @return
	 */
	public boolean isSessionClosed() {
		if (session.isClosing() || !session.isConnected()) {
			return true;
		}
		return false;
	}

	public IoSession getSession() {
		return session;
	}

	public String getCarID() {
		return carId;
	}

	/**
	 * 关闭会话
	 */
	public void close() {
		if (session != null) {
			session.close(false);
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param obj
	 * @return
	 */
	// private int sendPeriod = 3000;
	// private Lock sendLock = new ReentrantLock();

	// public boolean send(SocketMessage msg) {
	// sendLock.lock();
	// try {
	// int j = 0;
	// while (j < 3) {
	// long now = System.currentTimeMillis();
	// long dt = now - this.getLastActiveTime();
	// if (dt < sendPeriod) {
	// try {
	// Thread.sleep(sendPeriod - dt);
	// } catch (Exception e) {
	// }
	// j++;
	// } else {
	// break;
	// }
	// }
	// try {
	// session.write(msg);
	// this.setLastActiveTime(System.currentTimeMillis());
	// log.info("send msg," + msg.dump());
	// return true;
	// } catch (Exception e) {
	// log.error("fail to write message," + msg.dump(), e);
	// return false;
	// }
	// } catch (Exception e2) {
	// return false;
	// } finally {
	// sendLock.unlock();
	// }
	// }

	public boolean send(SocketMessage msg) {
		try {
			try {
				session.write(msg);
				log.info("send msg," + msg.dump());
				return true;
			} catch (Exception e) {
				log.error("fail to write message," + msg.dump(), e);
				return false;
			}
		} catch (Exception e2) {
			return false;
		} finally {
		}
	}

	public long getLastActiveTime() {
		return this.lastActiveTime;
	}

	public void setLastActiveTime(long t) {
		this.lastActiveTime = t;
	}
}
