package com.bkht.mina.trade;

import com.bkht.mina.utils.ByteTools;
import com.bkht.mina.utils.StringTools;

//查询终端属性应答
public class T0x0107 extends TradeInAbstract {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bkht.mina.trade.TradeInAbstract#unpackBody()
	 */
	@Override
	public void unpackBody() throws Exception {
		byte[] buff = wrapper.getMsgBody();
		// 终端类型
		byte[] typeBytes = new byte[2];
		System.arraycopy(buff, 0, typeBytes, 0, 2);
		// TODO

		// 制造商ID
		byte[] manufactureBytes = new byte[5];
		System.arraycopy(buff, 2, manufactureBytes, 0, 5);
		String manufacture = StringTools.getString(manufactureBytes);
		// 终端类型
		byte[] manuTypeBytes = new byte[20];
		System.arraycopy(buff, 7, manuTypeBytes, 0, 20);
		String manuType = StringTools.getString(manuTypeBytes);
		// 终端ID
		byte[] terminalIdBytes = new byte[7];
		System.arraycopy(buff, 27, terminalIdBytes, 0, 7);
		String terminalId = StringTools.getString(terminalIdBytes);
		// 终端SIM 卡CCID
		byte[] ccidBytes = new byte[10];
		System.arraycopy(buff, 34, ccidBytes, 0, 10);
		byte[] ascBytes = ByteTools.bcd2Asc(ccidBytes, false);
		String ccid = StringTools.getString(ascBytes);

		// 终端硬件版本号长度
		byte versionLenByte = buff[44];
		int versionLen = (int) (versionLenByte & 0xff);

		byte[] versionBytes = new byte[versionLen];
		System.arraycopy(buff, 45, versionBytes, 0, versionLen);
		String version = StringTools.getString(versionBytes);

		// 终端固件版本号长度
		byte gujianLenByte = buff[45 + versionLen];
		int gujianLen = (int) (gujianLenByte & 0xff);

		byte[] gujianBytes = new byte[gujianLen];
		System.arraycopy(buff, 45 + versionLen + 1, gujianBytes, 0, gujianLen);
		String gujian = StringTools.getString(gujianBytes);

		// GNSS 模块属性
		byte gnssByte = buff[46 + versionLen + gujianLen];

		String gnss = ByteTools.bytesToBit(new byte[] { gnssByte });

		// 通信模块属性
		byte commprosBytes = buff[47 + versionLen + gujianLen];
		String commpros = ByteTools.bytesToBit(new byte[] { commprosBytes });

		logger.info(
				"终端类型:{}, 制造商ID:{}, 终端型号:{}, 终端ID:{}, ICCID:{}, 终端硬件版本号长度:{}, 终端硬件版本号:{}, 终端固件版本号长度:{}, 终端硬件版本号:{}, GNSS:{}, 通信属性:{}",
				null, manufacture, manuType, terminalId, ccid, versionLen,
				version, gujianLen, gujian, gnss, commpros);
	}
}
