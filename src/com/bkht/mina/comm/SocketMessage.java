package com.bkht.mina.comm;

import java.util.Calendar;

public class SocketMessage {

	private byte[] buffer;
	private int size;

	public static final int MAX_BUFFER = 128;
	public static final short sign = (short) 61568; // F0 80

	public SocketMessage() {
		buffer = new byte[MAX_BUFFER];
		size = 0;
	}

	public SocketMessage(int size, byte[] abuffer) throws Exception {
		this.size = size;
		buffer = new byte[size];
		System.arraycopy(abuffer, 0, buffer, 0, size);
	}

	public SocketMessage Clone() throws Exception {
		return new SocketMessage(this.size, buffer);
	}

	public int getBufferLength() {
		return size;
	}


	public void addByte(byte abyte) throws Exception {
		addByteInner(abyte);
	}

	private void addByteInner(byte abyte) throws Exception {
		if (size >= buffer.length)
			throw new Exception("Socket buffer Overflow");
		buffer[size++] = abyte;
	}

	public byte getByte(int pos) throws Exception {
		if (pos < 0 || pos >= size)
			throw new Exception("Socket buffer Overflow");
		return buffer[pos];
	}

	public static void setShort(byte[] buff, int pos, short num)
			throws Exception {
		if (pos > buff.length - 2)
			throw new Exception("Socket buffer Overflow");
		buff[pos] = (byte) ((num >> 8) & 0xff);
		buff[pos + 1] = (byte) (num & 0xff);
	}

	public static short getShort(byte[] buff, int pos) {
		if (pos > buff.length - 2 || pos < 0)
			try {
				throw new Exception("Socket buffer Overflow");
			} catch (Exception e) {
				e.printStackTrace();
			}

		int num = 0;
		for (int ix = 0; ix < 2; ++ix) {
			num <<= 8;
			num |= (buff[pos + ix] & 0xff);
		}
		return (short) num;
	}

	public void addShort(short num) throws Exception {
		if (size > buffer.length - 2)
			throw new Exception("Socket buffer Overflow");
		buffer[size] = (byte) ((num >> 8) & 0xff);
		buffer[size + 1] = (byte) (num & 0xff);
		size += 2;
	}

	public short getShort(int pos) throws Exception {
		if (pos > size - 2 || pos < 0)
			throw new Exception("Socket buffer Overflow");

		int num = 0;
		for (int ix = 0; ix < 2; ++ix) {
			num <<= 8;
			num |= (buffer[pos + ix] & 0xff);
		}
		return (short) num;
	}

	public static void setInt(byte[] buff, int pos, int num) throws Exception {
		if (pos > buff.length - 4)
			throw new Exception("Socket buffer Overflow");
		buff[pos] = (byte) ((num >> 24) & 0xff);
		buff[pos + 1] = (byte) ((num >> 16) & 0xff);
		buff[pos + 2] = (byte) ((num >> 8) & 0xff);
		buff[pos + 3] = (byte) (num & 0xff);
	}

	public static int getInt(byte[] buff, int pos) throws Exception {
		if (pos > buff.length - 4 || pos < 0)
			throw new Exception("Socket buffer Overflow");

		int num = 0;
		for (int ix = 0; ix < 4; ++ix) {
			num <<= 8;
			num |= (buff[pos + ix] & 0xff);
		}
		return num;
	}

	public void addInt(int num) throws Exception {
		addIntInner(num);
	}

	private void addIntInner(int num) throws Exception {
		if (size > buffer.length - 4)
			throw new Exception("Socket buffer Overflow");
		buffer[size] = (byte) ((num >> 24) & 0xff);
		buffer[size + 1] = (byte) ((num >> 16) & 0xff);
		buffer[size + 2] = (byte) ((num >> 8) & 0xff);
		buffer[size + 3] = (byte) (num & 0xff);
		size += 4;
	}

	public int getInt(int pos) throws Exception {
		if (pos > size - 4 || pos < 0)
			throw new Exception("Socket buffer Overflow");

		int num = 0;
		for (int ix = 0; ix < 4; ++ix) {
			num <<= 8;
			num |= (buffer[pos + ix] & 0xff);
		}
		return num;
	}

	public void addTime(long timemills) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timemills);

		addByteInner((byte) (cal.get(Calendar.YEAR) - 2000)); // 14,15
		addByteInner((byte) (cal.get(Calendar.MONTH) + 1)); // 1..12
		addByteInner((byte) cal.get(Calendar.DAY_OF_MONTH)); // 1..31
		addByteInner((byte) cal.get(Calendar.HOUR_OF_DAY)); // 0..23
		addByteInner((byte) cal.get(Calendar.MINUTE)); // 0..59
		addByteInner((byte) cal.get(Calendar.SECOND)); // 0..59
	}

	public long getTime(int pos) throws Exception {
		if (pos > size - 6 || pos < 0)
			throw new Exception("Socket buffer Overflow");
		int year = 2000 + getByte(pos);
		int month = getByte(pos + 1) - 1;
		int day = getByte(pos + 2);
		int hour = getByte(pos + 3);
		int minute = getByte(pos + 4);
		int second = getByte(pos + 5);
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute, second);
		return cal.getTimeInMillis();
	}

	public void addCarKey(byte[] key) throws Exception {
		for (int i = 0; i < 10; i++) {
			if (i < key.length)
				addByteInner(key[i]);
			else
				addByteInner((byte) 0);
		}
	}

	public byte[] getCarKey(int pos) throws Exception {
		if (pos > size - 10 || pos < 0)
			throw new Exception("Socket buffer Overflow");
		byte[] keys = new byte[10];
		int p = 0;
		for (int i = 0; i < 10; i++) {
			byte b = getByte(pos + i);
			if (b == (byte) 0)
				break;
			keys[p++] = b;
		}
		if (p == 10)
			return keys;
		byte[] ret = new byte[p];
		System.arraycopy(keys, 0, ret, 0, p);
		return ret;
	}

	public void addRfid(String str) throws Exception {
		if (size > buffer.length - 4)
			throw new Exception("Socket buffer Overflow");
		if (str.length() != 8)
			throw new Exception("Bad rfid nummber:" + str);
		int i = 0;
		while (i < 8) {
			String hex = str.substring(i, i + 2);
			int val = Integer.valueOf(hex, 16);
			addByteInner((byte) val);
			i += 2;
		}
	}

	public String getRfid(int pos) throws Exception {
		if (pos > size - 4 || pos < 0)
			throw new Exception("Socket buffer Overflow");
		String str = "";
		for (int i = 0; i < 4; i++) {
			byte idtemp = getByte(pos + i);
			int x = idtemp & 0xff;
			String hexstemp = Integer.toHexString(x);
			str += hexstemp;
		}
		return str;
	}

	public void addDouble(double value) throws Exception {
		if (size > buffer.length - 8)
			throw new Exception("Socket buffer Overflow");
		int i1 = (int) value;
		double dVal = value - i1;
		int i2 = (int) (dVal * 10000000);
		addIntInner(i1);
		addIntInner(i2);
	}

	public double getDouble(int pos) throws Exception {
		if (pos > size - 8 || pos < 0)
			throw new Exception("Socket buffer Overflow");
		int i1 = getInt(pos);
		int i2 = getInt(pos + 4);
		double dVal = i2 / 10000000.0;
		return i1 + dVal;
	}

	public byte[] getBytes() throws Exception {
		byte[] ret = new byte[size];
		System.arraycopy(buffer, 0, ret, 0, size);
		return ret;
	}

	public byte getTag() throws Exception {
		return getByte(0);
	}

	public int getID() throws Exception {
		return getInt(1);
	}

	public int getCarID() throws Exception {
		return getInt(5);
	}

	public String dump() {
		try {
			return "tag:" + getTag() + ",ID:" + getID() + ",carid:"
					+ getCarID();
		} catch (Exception e) {
			return "Bad Message!";
		}
	}

	public String dumpHex() {
		String ret = "\n";
		for (int j = 0; j < buffer.length; j++) {
			if (j >= size)
				break;
			int x = buffer[j] & 0xff;
			String hex = Integer.toHexString(x);
			if (hex.length() == 1)
				hex = '0' + hex;
			ret += hex;
			if (j % 10 == 9)
				ret += '\n';
			else
				ret += ' ';
		}
		return ret;
	}


}
