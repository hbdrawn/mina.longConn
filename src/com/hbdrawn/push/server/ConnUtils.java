package com.hbdrawn.push.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.hbdrawn.cache.ClusterCache;

//连接工具类

public class ConnUtils {

	private static Logger logger = LoggerFactory.getLogger(ConnUtils.class);

	public static void closeSession(IoSession session) {
		String remoteAddress = (String) session.getAttribute("sessionClients");
		if (remoteAddress != null) {
			String[] clients = remoteAddress.split("\\|");
			for (String client : clients) {
				// ClusterCache.getDefaultHazelcastInstance().getMap("IoSession")
				// .remove(client);
				// ClusterCache.getDefaultHazelcastInstance().getMap("IoSession")
				// .remove(client + "#IoSession");
				logger.info("集群式共享内存 remove:{}-{}", session.getRemoteAddress(),
						client);
			}
		}
		session.close(true);
	}
}
