package com.bkht.mina.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.bkht.mina.msg.BodyPros;
import com.bkht.mina.msg.MsgPackage;
import com.bkht.mina.msg.MsgWrapper;
import com.bkht.mina.server.VehicleSessionHolder;
import com.bkht.mina.trade.M0x8108;
import com.bkht.mina.trade.TradeOut;

public class FileUtils {

	// 文件名+第几包
	public static byte[] getFile(int num, String path) throws Exception {
		if (num <= 0) {
			throw new Exception("获取的包数错误:" + num);
		}
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
			channel = fs.getChannel();
			int len = (int) channel.size();
			int maxPackageNum = (int) Math.ceil(len
					/ (double) M0x8108.MAX_LEN_BODY);
			if (num > maxPackageNum) {
				throw new Exception("索取包号超出最大长度:[" + num + ">" + maxPackageNum
						+ "]");
			}
			int bufferLen = M0x8108.MAX_LEN_BODY;
			int position = (num - 1) * M0x8108.MAX_LEN_BODY;
			if (num == maxPackageNum) {
				bufferLen = len - position;
			}
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferLen);
			channel.position(position);
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendToVetical(M0x8108 mx, String carId, String path)
			throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			throw new Exception("文件不存在:" + path);
		}

		long length = file.length();
		int maxPackageNum = (int) Math.ceil(length
				/ (double) M0x8108.MAX_LEN_BODY);
		for (int i = 1; i <= maxPackageNum; i++) {
			byte[] bs = getFile(i, path);
			mx.setPackageBody(bs);
			byte[] body = mx.getBody();
			MsgWrapper wrapper = new MsgWrapper("8108", new BodyPros("1",
					"000", (short) body.length), carId, (short) 1,
					new MsgPackage((short) maxPackageNum, (short) i), body);
			TradeOut trade = new TradeOut(wrapper);
			trade.doHandler(VehicleSessionHolder.getSession(carId));
			Thread.sleep(5000);
		}

	}

	public static short getLenFromFile(String path) {
		File file = new File(path);
		long len = file.length();
		return (short) Math.ceil(len / (double) M0x8108.MAX_LEN_BODY);
	}

	// public static void main(String[] args) throws Exception {
	// String path = "E:/git/mina.longConn/resources/tt.txt";
	// byte[] file = getFile(2, path);
	// System.out.println(StringTools.toHexTable(file));
	// }
}
