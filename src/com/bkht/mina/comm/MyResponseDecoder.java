package com.bkht.mina.comm;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bkht.mina.utils.StringTools;

public class MyResponseDecoder extends CumulativeProtocolDecoder {
	private Logger logger = LoggerFactory.getLogger(MyResponseDecoder.class);

	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		logger.debug("接收到报文，即将处理：\n{}", StringTools.toHexTable(in.array()));
		int start = in.position();
		byte previous = 0x00;
		while (in.hasRemaining()) {
			byte current = in.get();
			if (previous == ProtocolUtils.FLAG && current == ProtocolUtils.FLAG) {
				int position = in.position();
				int limit = in.limit();
				try {
					in.position(start);
					in.limit(position);
					// 在position和limit之间的报文为完整的报文
					byte[] buffer = parseCommand(in);
					if (buffer.length < 12) {
						throw new Exception("报文长度错误:" + buffer);
					}
					// 第一步：进行转义还原TODO
					buffer = decode(buffer);
					// 第二步，进行校验TODO
					verify(buffer);
					SocketMessage msg = new SocketMessage(buffer.length, buffer);
					out.write(msg);
					previous = 0x00;
					start = 0;
				} finally {
					// 复原
					in.position(position);
					in.limit(limit);
				}
				return true;
			} else if (previous == 0x00 && current == ProtocolUtils.FLAG) {
				// 第一个oxfe处理
				previous = ProtocolUtils.FLAG;
				start = in.position();
			}
			// previous = current;
		}

		in.position(start);
		logger.warn("网络延迟，继续等待接收数据");
		return false;
	}

	private byte[] parseCommand(IoBuffer in) {
		byte[] result = new byte[in.limit() - in.position() + 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = in.get(in.position() - 1 + i);
		}
		return result;
	}

	// 根据协议进行解码，即转义还原操作
	private byte[] decode(byte[] buffer) throws Exception {
		// 标志位是否正确
		if (buffer[0] != ProtocolUtils.FLAG
				|| buffer[buffer.length - 1] != ProtocolUtils.FLAG) {
			throw new Exception("报文内容非法：开始或结束标志位不是0xfe");
		}
		// 去除标志位
		byte[] newBufferNoSighn = new byte[buffer.length - 2];
		System.arraycopy(buffer, 1, newBufferNoSighn, 0, buffer.length - 2);
		// 进行转义还原
		byte[] newBuffer = new byte[buffer.length - 2];
		// 长度计数
		int j = 0;
		for (int i = 0; i < newBufferNoSighn.length; i++) {
			if (i != newBufferNoSighn.length - 1
					&& newBufferNoSighn[i] == ProtocolUtils.ESCAP) {
				if (newBufferNoSighn[i + 1] == ProtocolUtils.ESCAP_02) {
					newBuffer[j] = ProtocolUtils.FLAG;
					i++;
				} else if (newBufferNoSighn[i + 1] == ProtocolUtils.ESCAP_01) {
					newBuffer[j] = ProtocolUtils.ESCAP;
					i++;
				} else {
					newBuffer[j] = newBufferNoSighn[i];
				}
			} else {
				// 将最后一位复制进来
				newBuffer[j] = newBufferNoSighn[i];
			}
			j++;
		}
		byte[] handlerBuffer = new byte[j];
		System.arraycopy(newBuffer, 0, handlerBuffer, 0, j);
		logger.info("报文转义还原完毕:\n{}", StringTools.toHexString(handlerBuffer));
		return handlerBuffer;
	}

	private void verify(byte[] buffer) throws Exception {
		byte result = 0x00;
		for (int i = 0; i < buffer.length - 1; i++) {
			result = (byte) (result ^ buffer[i]);
		}
		if (result != buffer[buffer.length - 1]) {
			throw new Exception("数据校验错误");
		}
		logger.info("数据校验成功");
	}
}
