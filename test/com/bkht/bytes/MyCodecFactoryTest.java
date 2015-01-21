package com.bkht.bytes;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.serialization.ObjectSerializationEncoder;

import com.bkht.mina.comm.MyResponseDecoder;

public class MyCodecFactoryTest implements ProtocolCodecFactory {
	private static ProtocolEncoder encoder = new ObjectSerializationEncoder();
	private static ProtocolDecoder decoder = new MyResponseDecoder();

	public MyCodecFactoryTest() {
	}

	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
		return decoder;
	}
}
