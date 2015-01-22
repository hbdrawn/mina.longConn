package com.bkht.mina.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static byte[] getTime(String time) throws Exception {
		if (time == null) {
			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
			SimpleDateFormat sf = new SimpleDateFormat("yy-MM-DD-hh-mm-ss");
			time = sf.format(date);
		}

		time = time.replaceAll("\\-", "");
		// byte[] timeBytes = new byte[6 * 2];
		// int j = 0;
		// for (int i = 0; i < split.length; i++) {
		// short tmp = Short.valueOf(split[i]);
		// byte[] buff = new byte[2];
		// SocketMessage.setShort(buff, 0, tmp);
		// timeBytes[j] = buff[0];
		// timeBytes[j + 1] = buff[1];
		// j += 2;
		// }
		// return ByteTools.asc2Bcd(timeBytes);
		return StringTools.string2Byte(time);
		// return timeBytes;
	}

	public static String setTime(byte[] timeBytes) throws Exception {
		// byte[] bcd2Asc = ByteTools.bcd2Asc(timeBytes, true);
		// short[] date = new short[6];
		// int j = 0;
		// for (int i = 0; i < 6; i++) {
		// date[j++] = SocketMessage.getShort(bcd2Asc, i * 2);
		// }

		// StringBuffer sb = new StringBuffer();
		// for (int i = 0; i < date.length; i++) {
		// sb.append(String.valueOf(date[i]));
		// if (i != date.length - 1) {
		// sb.append("-");
		// }
		// }
		StringBuffer sb = new StringBuffer();
		byte[] bytes = new byte[1];
		for (int i = 0; i < timeBytes.length; i++) {
			bytes[0] = timeBytes[i];
			sb.append(StringTools.toHexString(bytes));
			if (i != 5) {
				sb.append("-");
			}
		}
		return sb.toString();
	}

	// public static void main(String[] args) throws Exception {
	// System.out.println(StringTools.toHexTable(TimeUtils.getTime(null)));
	// }
}
