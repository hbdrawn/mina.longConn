package com.bkht.mina.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bkht.mina.comm.SessionWrap;

public class VehicleSessionHolder {
	private static Map<String, SessionWrap> carSessionMap = new ConcurrentHashMap<String, SessionWrap>();

	public static void putSession(SessionWrap sw) {
		String carID = sw.getCarID();
		SessionWrap sessionWrap = carSessionMap.get(carID);
		if (sessionWrap != null) {
			sessionWrap.close();
		}
		carSessionMap.put(carID, sw);
	}

	public static SessionWrap getSession(String cardId) throws Exception {
		SessionWrap session = carSessionMap.get(cardId);
		if (session == null) {
			throw new Exception("发送队列为空，尚未有终端建立连接");
		}
		return session;
	}
}
