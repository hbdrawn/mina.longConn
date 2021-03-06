package com.bkht.bytes;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bkht.mina.server.VehicleSocketHandler;
import com.bkht.mina.utils.StringTools;

public class TestMsg {

	IoSession session = null;

	@Before
	public void init() {
		IoConnector connector = new NioSocketConnector();
		connector.setHandler(new VehicleSocketHandler());
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast("codec",
				new ProtocolCodecFilter(new MyCodecFactoryTest()));

		SocketSessionConfig config = (SocketSessionConfig) connector
				.getSessionConfig();
		config.setReadBufferSize(2048);
		config.setReuseAddress(true);
		config.setUseReadOperation(true);
		InetSocketAddress address = new InetSocketAddress(
				Integer.parseInt("9999"));
		ConnectFuture connFuture = connector.connect(address);
		connFuture.awaitUninterruptibly();
		session = connFuture.getSession();
	}

	@After
	public void finish() {
		long start = System.currentTimeMillis();
		while (true) {
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis - start > 1000 * 30) {
				break;
			}
		}
	}

	@Test
	public void testSheBeiZhuCe() throws Exception {
		String msg = "7E010000371310132400000003363130305944545400636172207465726D696E616C52656E74"
				+ "0000000013101324000000003838383838383838383838383838383838006E7E";

		byte[] buffer = StringTools.string2Byte(msg);
		session.write(buffer);
	}

	@Test
	public void testSheBeiZhuXiao() throws Exception {
		String msg = "7E00030000131013240000000B3C7E";
		byte[] buffer = StringTools.string2Byte(msg);
		session.write(buffer);
	}

	@Test
	public void testKaijiJianQuan() throws Exception {
		String msg = "7E0501001D1310132400000004AABBCCDD13101324013839383630313133393231313039353634333132157E";
		byte[] buffer = StringTools.string2Byte(msg);
		session.write(buffer);
	}

	@Test
	public void testWeiZhiXinxiShangchuan() throws Exception {
		String msg = "7E0504002E131013240000000C00000000000410030261CFB906EF6212004100000000150119155707000000000000698C00100000000000000000707E";
		byte[] buffer = StringTools.string2Byte(msg);
		session.write(buffer);
	}

	@Test
	public void testWeiZhiXinxiShangchuan2() throws Exception {
		String msg = "7E0504002E131013240000002900000000000410130261D04C06EF6280003200000085150119162840000000000000698C00100000000000000000F07E";
		byte[] buffer = StringTools.string2Byte(msg);
		session.write(buffer);
	}
	
	
}
