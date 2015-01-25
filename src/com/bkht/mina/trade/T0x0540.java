package com.bkht.mina.trade;

import org.apache.mina.core.session.IoSession;

import com.bkht.mina.comm.SocketMessage;
import com.bkht.mina.utils.StringTools;

// 终端自动要包
public class T0x0540 extends TradeInAbstract {

	private int type;
	private String manuId;
	private String version;
	private short packageNum;
	private short num;

	@Override
	public void unpackBody() throws Exception {
		byte[] body = wrapper.getMsgBody();
		// 升级类型
		byte typeByte = body[0];
		type = (int) (typeByte & 0xff);
		// 制造商ID
		byte[] manuIdBytes = new byte[5];
		System.arraycopy(body, 1, manuIdBytes, 0, 5);
		manuId = StringTools.getString(manuIdBytes);
		// 版本号长度
		byte versionLenByte = body[6];
		int versionLen = (int) (versionLenByte & 0xff);

		byte[] versionBytes = new byte[versionLen];
		System.arraycopy(body, 7, versionBytes, 0, versionLen);
		version = StringTools.getString(versionBytes);

		// 总包数
		packageNum = SocketMessage.getShort(body, 7 + versionLen);
		// 索要包号
		num = SocketMessage.getShort(body, 9 + versionLen);

		logger.debug(">>>升级类型:{}, 制造商ID:{}, 版本号长度:{}, 版本号:{}, 总包数:{}, 索要包号:{}",
				type, manuId, versionLen, version, packageNum, num);
	}

	@Override
	public void response(IoSession session) throws Exception {
		// TODO
		logger.warn("<<< for test");
		M0x8108 mx8108 = new M0x8108();
		mx8108.setManuId(manuId);
		mx8108.setType(type);
		mx8108.setVersion(version);
		byte[] packageBytes = new byte[100];
		mx8108.setPackageBody(packageBytes);
		session.write(mx8108.getBody());
	}
}
