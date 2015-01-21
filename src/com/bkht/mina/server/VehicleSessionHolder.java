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

	public static SessionWrap getSession(String cardId) {
		return carSessionMap.get(cardId);
	}
}
