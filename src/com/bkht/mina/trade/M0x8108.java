package com.bkht.mina.trade;

public class M0x8108 implements MsgAbstract {

	private int type; // 升级类型 0：终端，12：道路运输证IC 卡读卡器，52：北斗卫星定位模块

	private String manuId; // 制造商ID

	private String version; // 版本号

	private byte[] packageBody; // 升级的数据包

	public byte[] getBody() throws Exception {
		int len = 7 + version.getBytes().length + 1 + packageBody.length;
		if (len > 1022) {
			throw new Exception("数据包长度不合法(>1022):" + len);
		}
		byte[] buff = new byte[len];
		buff[0] = (byte) type;
		System.arraycopy(manuId.getBytes(), 0, buff, 1, 5);
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
