package com.bkht.mina.comm;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.utils.StringTools;

public class MyRequestEncoder implements ProtocolEncoder {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		SocketMessage msg = (SocketMessage) message;
		byte[] bytes = msg.getBytes();
		// 加校验位，并添加标志位
		bytes = verify(bytes);
		// 转义
		bytes = encode(bytes);
		IoBuffer buffer = IoBuffer.allocate(bytes.length + 2, false);
		buffer.put(ProtocolUtils.FLAG);
		// buffer.put((byte) 0x80);
		// buffer.put((byte) (bytes.length >> 8));
		// buffer.put((byte) (bytes.length));
		buffer.put(bytes, 0, bytes.length);
		buffer.put(ProtocolUtils.FLAG);
		buffer.flip();
		out.write(buffer);
	}

	private byte[] verify(byte[] bytes) {
		byte[] newBuffer = new byte[bytes.length + 1];
		byte result = 0x00;
		for (int i = 0; i < bytes.length; i++) {
			result = (byte) (result ^ bytes[i]);
			newBuffer[i] = bytes[i];
		}
		newBuffer[bytes.length] = result;
		return newBuffer;
	}

	public void dispose(IoSession session) throws Exception {
	}

	private byte[] encode(byte[] buffer) throws Exception {
		// 标志位是否正确
		// if (buffer[0] != ProtocolUtils.FLAG
		// || buffer[buffer.length - 1] != ProtocolUtils.FLAG) {
		// throw new Exception("报文内容非法：开始或结束标志位不是0xfe");
		// }
		// 去除标志位
		byte[] newBuffer = new byte[buffer.length * 2];
		// System.arraycopy(buffer, 1, newBuffer, 0, buffer.length - 1);
		// 进行转义
		// 长度计数
		int j = 0;
		for (int i = 0; i < buffer.length; i++) {
			newBuffer[j] = buffer[i];
			if (buffer[i] == ProtocolUtils.FLAG) {
				newBuffer[j + 1] = ProtocolUtils.ESCAP;
				newBuffer[j + 2] = ProtocolUtils.ESCAP_02;
				j += 2;
			} else if (buffer[i] == ProtocolUtils.ESCAP) {
				newBuffer[j + 1] = ProtocolUtils.ESCAP_01;
				j++;
			}
		}
		byte[] handlerBuffer = new byte[j + 2];
		System.arraycopy(newBuffer, 0, handlerBuffer, 1, j);
		handlerBuffer[0] = ProtocolUtils.FLAG;
		handlerBuffer[j + 1] = ProtocolUtils.FLAG;
		logger.info("报文转义完毕:\n{}", StringTools.toHexString(handlerBuffer));
		return handlerBuffer;
	}
}
