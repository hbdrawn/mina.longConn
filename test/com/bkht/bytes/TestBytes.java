package com.bkht.bytes;

public class TestBytes {

	public static void main(String[] args) {
		byte[] buffer = new byte[20];
		buffer[13] = 0x00;
		// System.arraycopy(Arrays.asList(0x00), 0, buffer, 0, 1);
		System.arraycopy("abcdefg".getBytes(), 0, buffer, 0, 7);
		System.out.println(new String(buffer));
		System.out.println(buffer.length);
	}
}
