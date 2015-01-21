package com.bkht.mina.utils;

public class ByteTools {

	/**
	 * Byte转Bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	/**
	 * Bit转Byte
	 */
	public static byte BitToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

	/**
	 * ASCII码转BCD码:默认右填充0
	 * 
	 * @param srcData
	 *            ASC字节数组
	 * @return 转化后的BCD字节数组
	 */
	public static byte[] asc2Bcd(byte[] srcData) {
		int len = srcData.length;
		if (len % 2 == 1) {
			byte[] tmpArrray = new byte[len + 1];
			System.arraycopy(srcData, 0, tmpArrray, 0, len - 1);
			tmpArrray[len] = srcData[len - 1];
			srcData = tmpArrray;
		}
		int size = srcData.length / 2;
		byte[] retArray = new byte[size];
		for (int i = 0; i < size; i++) {
			int high = 0;
			if (srcData[2 * i] >= 'a' && srcData[2 * i] <= 'f') {
				high = srcData[2 * i] - 'a' + 10;
			} else if (srcData[2 * i] >= 'A' && srcData[2 * i] <= 'F') {
				high = srcData[2 * i] - 'A' + 10;
			} else {
				high = srcData[2 * i] & 0x0f;
			}
			int low = 0;
			if (srcData[2 * i + 1] >= 'a' && srcData[2 * i + 1] <= 'f') {
				low = srcData[2 * i + 1] - 'a' + 10;
			} else if (srcData[2 * i + 1] >= 'A' && srcData[2 * i + 1] <= 'F') {
				low = srcData[2 * i + 1] - 'A' + 10;
			} else {
				low = srcData[2 * i + 1] & 0x0f;
			}
			retArray[i] = (byte) ((high << 4) + low);
		}
		return retArray;
	}

	/**
	 * 将BCD码转ASCII码
	 * 
	 * @param srcData
	 *            BCD字节数组
	 * @param lowercase
	 *            是否小写
	 * @return 转化后的ASC字节数组
	 */
	public static byte[] bcd2Asc(byte[] srcData, boolean lowercase) {
		if (srcData == null || srcData.length == 0) {
			return null;
		}
		int length = srcData.length;
		byte[] ascArray = new byte[length * 2];
		for (int i = 0; i < length; i++) {
			int bcd = srcData[i];
			if (bcd < 0) {
				bcd += 256;
			}
			byte high = (byte) (bcd >>> 4);
			if (0 <= high && high <= 9) {
				high += 48;
			} else {
				if (lowercase) {
					high += 'a' - 10;
				} else {
					high += 'A' - 10;
				}
			}
			ascArray[i * 2] = high;
			byte low = (byte) (bcd & 0x0f);
			if (0 <= low && low <= 9) {
				low += 48;
			} else {
				if (lowercase) {
					low += 'a' - 10;
				} else {
					low += 'A' - 10;
				}
			}
			ascArray[i * 2 + 1] = low;
		}
		return ascArray;
	}

	public static byte[] getShort(short num) {
		byte[] buff = new byte[2];
		buff[0] = (byte) ((num >> 8) & 0xff);
		buff[1] = (byte) (num & 0xff);
		return buff;
	}
}
