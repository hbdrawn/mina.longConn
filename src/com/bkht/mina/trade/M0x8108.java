package com.bkht.mina.trade;

public class M0x8108 implements MsgAbstract {

	public static final int MAX_LEN = 1022;
	public static final int MAX_LEN_BODY = 1000;

	private int type; // 升级类型 0：终端，12：道路运输证IC 卡读卡器，52：北斗卫星定位模块

	private String manuId; // 制造商ID

	private String version; // 版本号

	private byte[] packageBody; // 升级的数据包

	public byte[] getBody() throws Exception {
		int len = 7 + version.getBytes().length + 1 + packageBody.length;
		if (len > MAX_LEN) {
			throw new Exception("数据包长度不合法(>1022):" + len);
		}
		byte[] buff = new byte[len];
		buff[0] = (byte) type;
		byte[] manuIdBytes = manuId.getBytes();
		// 对未满5字节时，右填充0
		if (manuIdBytes.length < 5) {
			byte[] tmp = new byte[5];
			System.arraycopy(manuIdBytes, 0, tmp, 0, manuIdBytes.length);
			for (int i = manuIdBytes.length; i < 5; i++) {
				tmp[i] = 0x00;
			}
			manuIdBytes = tmp;
		}
		System.arraycopy(manuIdBytes, 0, buff, 1, 5);
		byte[] versionBytes = version.getBytes();
		buff[6] = (byte) versionBytes.length;
		System.arraycopy(versionBytes, 0, buff, 7, versionBytes.length);
		buff[7 + versionBytes.length] = (byte) packageBody.length;
		System.arraycopy(packageBody, 0, buff, 7 + versionBytes.length + 1,
				packageBody.length);
		return buff;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getManuId() {
		return manuId;
	}

	public void setManuId(String manuId) {
		this.manuId = manuId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public byte[] getPackageBody() {
		return packageBody;
	}

	public void setPackageBody(byte[] packageBody) {
		this.packageBody = packageBody;
	}

}
