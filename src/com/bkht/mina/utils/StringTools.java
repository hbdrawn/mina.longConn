/**
 * Special Declaration: These technical material reserved as the technical 
 * secrets by Bankit TECHNOLOGY have been protected by the "Copyright Law" 
 * "ordinances on Protection of Computer Software" and other relevant 
 * administrative regulations and international treaties. Without the written 
 * permission of the Company, no person may use (including but not limited to 
 * the illegal copy, distribute, display, image, upload, and download) and 
 * disclose the above technical documents to any third party. Otherwise, any 
 * infringer shall afford the legal liability to the company.
 *
 * 特别声明：本技术材料受《中华人民共和国着作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，浙江宇信班克信息技术有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 *
 * Copyright(C) 2011 Bankit Tech, All rights reserved.
 */

/*
 * cn.com.bankit.toolkit.common.StringToolsTest.java
 * Created by dcz @ 2011-12-2 上午11:25:38
 */

package com.bkht.mina.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

/**
 * <DL>
 * <DT><B> 字符串工具类 </B></DT>
 * <p>
 * <DD>字符串与字节数组互转(显示不变),16进制表格字符串与字节数组互转,字符串空判断,获取对象字符串表示,16进制字符串转字节数组</DD>
 * </DL>
 * <p>
 * 
 * <DL>
 * <DT><B>使用范例</B></DT>
 * <p>
 * <DD>使用范例说明</DD>
 * </DL>
 * <p>
 * 
 * @author dcz $Author: wangmingsheng $
 * @author 浙江宇信班克信息技术有限公司
 * @version $Id: StringToolsTest.java 64 2011-12-05 02:30:04Z wangmingsheng $
 */
public class StringTools {

	/**
	 * 将byte[]数组转换成大写字符串,十六进制：01中文(30 31 D6 D0)转换后为：3031D6D0
	 * 
	 * @param data
	 *            字节数组
	 * @return 转换后字符串
	 */
	public static String byte2String(byte[] data) {
		return byte2String(data, Boolean.FALSE);
	}

	/**
	 * 将byte[]数组转换成字符串：显示信息表示不变
	 * 
	 * @param data
	 *            字节数组
	 * @param lowercase
	 *            是否转换成小写字符串
	 * @return 转换后字符串
	 */
	public static String byte2String(byte[] data, boolean lowercase) {
		if (data == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			String tmpStr = Integer.toHexString(data[i] & 0xff);
			tmpStr = StringUtils.leftPad(tmpStr, 2, CHAR_ZERROR);
			builder.append(tmpStr);
		}
		if (!lowercase) {
			return StringUtils.upperCase(builder.toString());
		}
		return builder.toString();
	}

	/**
	 * 16进制字符串(无0x)转换成byte[] byte2String方法的反转
	 * 
	 * @param srcData
	 *            转换前的字符串
	 * @return 转换后的byte[]
	 * @throws Exception
	 */
	public static byte[] string2Byte(String srcData) throws Exception {
		if (srcData == null) {
			return null;
		} else if (srcData.length() % 2 != 0) {
			throw new Exception("源数据的长度为奇数:" + srcData.length());
		}
		// byte[] tmpArray = srcData.getBytes();
		// int length = tmpArray.length / 2;
		// byte[] retArray = new byte[length];
		// for (int i = 0; i < length; i++)
		// {
		// // Character.digit(char , int radix);0--f,此范围外的字符异常
		// byte byte0 = Byte.decode(
		// "0x" + new String(new byte[] { tmpArray[i * 2] }))
		// .byteValue();
		// byte byte1 = Byte.decode(
		// "0x" + new String(new byte[] { tmpArray[i * 2 + 1] }))
		// .byteValue();
		// retArray[i] = (byte) (((byte) byte0 << 4) ^ byte1);
		// }
		// return retArray;
		// n row(s) below edited by sundy @2012-7-18 下午4:32:16
		// 没必要先获取byte[],然后new String
		// Byte.decode的处理用的是Integer.parseInt
		int len = srcData.length() / 2;
		byte[] newData = new byte[len];
		for (int i = 0; i < len; i++) {
			newData[i] = (byte) ((Character.digit(srcData.charAt(i * 2), 16) << 4) ^ Character
					.digit(srcData.charAt(i * 2 + 1), 16));
		}
		return newData;
	}

	/**
	 * 默认截取字符串前38K数据，其后补16长度的省略号（日志输出时使用，防止输出过多的日志信息）<br>
	 * 若38K均为汉字，则实际字节数为38*2字节
	 * 
	 * @param src
	 *            待转换的字符串
	 * @return 可能被截取后的字符串，若小于38K，则原值返回，否则取前48K数据
	 */
	public static String toString(String src) {
		int max = MAX_OUTPUT_LENGTH;
		return toString(src, max);
	}

	/**
	 * 对字符串src取前max长度字符，若src长度小于max，则原串返回
	 * 
	 * @param src
	 *            待截取字符串
	 * @param max
	 *            截取范围，若为-1，则不做限制
	 * @return
	 */
	protected static String toString(String src, int max) {
		if (max == -1) {
			return src;
		}
		if (src.length() <= max) { // 判断String的长度而不是bytes的长度，可以避免处理半个汉字的情况
			return src;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(src.substring(0, max));
		sb.append("................");
		return sb.toString();
	}

	/**
	 * 字符串转换为Map对象
	 * 
	 * @param str
	 *            待转换字符串，eg. aaa=111,bbb=222
	 * @return Map对象
	 */
	public static Map<String, String> string2Map(String mapStr) {
		Map<String, String> map = new HashMap<String, String>();
		String split = ",";
		String[] entrys = mapStr.split(split);
		for (String entry : entrys) {
			String[] strs = entry.split("=");
			if (strs.length > 1) {
				map.put(strs[0].trim(), strs[1].trim());
			}
		}
		return map;
	}

	/**
	 * 将十六进制表格字符串转换成byte[]
	 * 
	 * @param srcData
	 *            转换前的十六进制表格字符串
	 * @return 转换后的byte[]
	 * @throws Exception
	 */
	public static byte[] hexTableString2Byte(String srcData) throws Exception {
		if (srcData == null) {
			return null;
		}
		int hexStartIndex = 0;
		if (srcData.indexOf(CHAR_BLANK) == 4) {
			hexStartIndex = 6;
		}
		String[] lineStrArray = StringUtils.split(srcData, '\n');
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lineStrArray.length; i++) {
			int hexEndIndex = StringUtils.indexOf(lineStrArray[i], ';');
			String hexPart = StringUtils.substring(lineStrArray[i],
					hexStartIndex, hexEndIndex - 1);
			String[] byte2StringArray = StringUtils.split(hexPart, ' ');
			for (int j = 0; j < byte2StringArray.length; j++) {
				builder.append(byte2StringArray[j]);
			}
		}
		return string2Byte(builder.toString());
	}

	/**
	 * 将生成的16字节的二进制串，再按照每字节高4位、低4位转换成对应的大写字符或数字(‘0’-‘9’,’A’-‘F’)，从而形成32字节的字符串。<br>
	 * eg.<br>
	 * 二进制串：0x3c 5b 34 5f 6c 2b 9a 0d 6f 7c 9a 7d 2b 5e 1c 0f<br>
	 * 转换为'3C5B345F6C2B9A0D6F7C9A7D2B5E1C0F'
	 * 
	 * @param bt
	 * @return String
	 */
	public static String toHexString(byte[] bt) {
		// StringBuffer sb = new StringBuffer();
		// for (int i = 0; i < bt.length; i++)
		// {
		// String s = Integer.toHexString(bt[i] & 0xFF);
		// if (s.length() == 1)
		// {
		// sb.append("0");
		// }
		// sb.append(s);
		// }
		// String s = sb.toString().toUpperCase();
		return byte2String(bt);
	}

	public static String toHexTable(byte[] data) {
		return toHexTable(data, defaultCharset);
	}

	/**
	 * 将byte[]转化为16进制表格字符串
	 * 
	 * @param data
	 *            字节数组
	 * @return 转化后的16进制表格字符串
	 */
	public static String toHexTable(byte[] data, String charset) {
		if (data.length <= MAX_OUTPUT_LENGTH) {
			return toHexTable(data, 16, 7, charset);
		}
		byte[] b = new byte[MAX_OUTPUT_LENGTH + 16];
		String suspension = "................";
		int srcLength = data.length;
		String length = String.valueOf(srcLength);
		suspension = length + suspension;
		String sus = suspension.substring(0,
				suspension.length() - length.length());
		System.arraycopy(data, 0, b, 0, MAX_OUTPUT_LENGTH);
		System.arraycopy(sus.getBytes(), 0, b, MAX_OUTPUT_LENGTH, 16);
		return toHexTable(b, 16, 7, charset);
	}

	public static final String defaultCharset = System.getProperty(
			"file.encoding", "UTF-8");

	/**
	 * 将byte[]转化为16进制表格字符串,包括左中右三部分输出
	 * <p>
	 * <B>注意:</B>请使用toHexTable(byte[] data, int numOfLine, int column,String
	 * charset)方法，谢谢.
	 * 
	 * @param data
	 *            字节数组
	 * @param column
	 *            输出内容：7是左中右都会输出,4只输出最左边，2只输出中间部分，1只输出最右边
	 * @return 转化后的16进制表格字符串
	 */
	public static String toHexTable(byte[] data, int numOfLine, int column) {
		return toHexTable(data, numOfLine, column, defaultCharset);
	}

	private static final char CHAR_BLANK = ' ';

	private static final char CHAR_ZERROR = '0';

	private static final String STR_COLON = ": ";

	private static final char CHAR_SEMICOLON = ';';

	private static final char CHAR_DOT = '.';

	private static final String STR_EMPTY = "";

	private static final char CHAR_NEWLINE = '\n';

	/** 打印单个变量的最大长度 */
	public final static int MAX_OUTPUT_LENGTH_SINGLE_VAR = 10240;

	public final static int MAX_OUTPUT_LENGTH = 38912;

	/** 字符编码值 */
	public static final String CHARSET_GBK = "GBK";

	public static final String CHARSET_GB2312 = "GB2312";

	public static final String CHARSET_GB18030 = "GB18030";

	public static final String CHARSET_UTF_8 = "UTF-8";

	public static final String CHARSET_UTF8 = "UTF8";

	/**
	 * 将byte[]转化为16进制表格字符串,包括左中右三部分输出
	 * 
	 * @param data
	 *            字节数组
	 * @param column
	 *            输出内容：7是左中右都会输出,4只输出最左边，2只输出中间部分，1只输出最右边
	 * @param charset
	 *            编码值
	 * @return 转化后的16进制表格字符串
	 */
	public static String toHexTable(byte[] data, int numOfLine, int column,
			String charset) {
		if (data == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(2048);
		int lines = data.length % numOfLine == 0 ? data.length / numOfLine
				: data.length / numOfLine + 1;

		int columnA = column & 4;
		int columnB = column & 2;
		int columnC = column & 1;

		for (int i = 0; i < lines; i++) {
			if (columnA == 4) {
				String lineCountPart = Integer.toString(i);
				lineCountPart = StringUtils.leftPad(lineCountPart, 4,
						CHAR_ZERROR);
				builder.append(lineCountPart);
				builder.append(STR_COLON);
			}
			int length = (data.length - i * numOfLine) > numOfLine ? numOfLine
					: (data.length - i * numOfLine);
			byte[] hexPartArray = new byte[length];
			System.arraycopy(data, i * numOfLine, hexPartArray, 0, length);
			if (columnB == 2) {

				StringBuilder hsb = new StringBuilder();
				for (int j = 0; j < length; j++) {
					String tmpStr = StringUtils.leftPad(
							Integer.toHexString(hexPartArray[j] & 0xff), 2,
							CHAR_ZERROR);
					hsb.append(tmpStr);
					if (j < numOfLine - 1) {
						hsb.append(CHAR_BLANK);
					}
				}
				String hexPart = StringUtils.rightPad(hsb.toString(), 47,
						CHAR_BLANK);
				builder.append(hexPart);
				builder.append(CHAR_SEMICOLON);
				builder.append(CHAR_BLANK);
			}
			if (columnC == 1) {
				builder.append(hex2String(hexPartArray, charset));
			}
			if (i == lines - 1) {
				break;
			}
			builder.append(CHAR_NEWLINE);
		}
		return builder.toString();
	}

	// 表1 汉字编码范围
	// 名称
	// 第一字节
	// 第二字节
	// GB2312
	// 0xB0-0xF7(176-247)
	// 0xA0-0xFE（160-254）
	// GBK
	// 0x81-0xFE（129-254）
	// 0x40-0xFE（64-254）
	// Big5
	// 0x81-0xFE（129-255）
	// 0x40-0x7E（64-126）
	// 0xA1－0xFE（161-254）
	// 参考的URL http://ir.hit.edu.cn/~taozi/bianma.htm
	// UTF8参考资料URL http://star.sgst.cn/questionDetail.do?id=45664

	private static String hex2String(byte[] hex, String charset) {
		int length = hex.length;
		StringBuilder sb = new StringBuilder(32);
		try {
			for (int j = 0; j < length; j++) {
				char ch1 = (char) hex[j];
				if (ch1 <= 32) {
					ch1 = CHAR_DOT;
				} else if (ch1 <= 128) {
					// ascII,直接输出
				} else {
					if (charset.equalsIgnoreCase(CHARSET_GB2312)) {
						if (j + 1 <= length - 1) {
							char ch2 = (char) hex[j + 1];
							if ((byte) ch1 > (byte) 0xb0
									&& (byte) ch1 < (byte) 0xf7
									&& (byte) ch2 > (byte) 0xa0
									&& (byte) ch2 < (byte) 0xf0) {
								sb.append(new String(hex, j, 2, charset));
								j++;
								continue;
							}
						}
					}
					if (charset.equalsIgnoreCase(CHARSET_GBK)
							|| charset.equalsIgnoreCase(CHARSET_GB18030)) {
						if (j + 1 <= length - 1) {
							char ch2 = (char) hex[j + 1];
							if ((byte) ch1 >= (byte) 0x81
									&& (byte) ch1 <= (byte) 0xfe
									&& ((byte) ch2 & 0xFF) >= (byte) 0x40
									&& (byte) ch2 <= (byte) 0xfe) {
								sb.append(new String(hex, j, 2, charset));
								j++;
								continue;
							}
						}
					} else if (charset.equalsIgnoreCase(CHARSET_UTF8)
							|| charset.equalsIgnoreCase(CHARSET_UTF_8)) {
						if (j + 2 <= length - 1) // 目前只考虑中文3字节情况
						{
							char ch2 = (char) hex[j + 1];
							char ch3 = (char) hex[j + 2];
							if ((byte) ch1 >= (byte) 0xe0
									&& (byte) ch2 >= (byte) 0x80) {
								String tmpStr = STR_EMPTY;
								if ((byte) ch3 >= (byte) 0x80) {
									tmpStr = new String(hex, j, 3, charset);
									j += 2;
								} else {
									tmpStr = new String(hex, j, 2, charset);
									j++;
								}
								sb.append(tmpStr);
								continue;
							}
						}
					}

				}
				sb.append(ch1);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO: 这里是要跑异常还是要打日志？
			e.printStackTrace();
			// throw new RuntimeException(e);
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否为空或者null
	 * 
	 * @param src
	 *            源字符串
	 * @return 判断结果
	 */
	public static boolean isEmpty(String src) {
		if (src == null || src.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取异常的所有信息
	 * 
	 * @param e
	 * @return
	 * @throws IOException
	 */
	public static String exception2String(Exception e) {
		if (e == null)
			return null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(os));
		byte[] msg = os.toByteArray();
		String str = new String(msg);
		try {
			os.close();
		} catch (IOException e1) {
		}
		return str;
	}

	/**
	 * 获取对象的字符串
	 * 
	 * @param obj
	 *            指定对象
	 * @return 对象对应的字符串
	 */
	public static String getString(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return new String((byte[]) obj);
		} else {
			return obj.toString();
		}
	}

	public static Integer getInt(Object obj) throws Exception {
		int i_obj;
		if (obj == null) {
			throw new Exception("值为null");
		} else if (obj instanceof Integer) {
			i_obj = (Integer) obj;
		} else if (obj instanceof String) {
			i_obj = Integer.parseInt((String) obj);
		} else if (obj instanceof byte[]) {
			i_obj = Integer.parseInt(new String((byte[]) obj));
		} else if (obj instanceof Byte) {
			i_obj = (Byte) obj & 0xff; // 如果为负数，可能是业务人员自己使用错误，该正负由业务人员自己处理
		} else if (obj instanceof Short) {
			i_obj = (Short) obj;
		} else if (obj instanceof Long) {
			i_obj = ((Long) obj).intValue();
		} else if (obj instanceof Double) {
			i_obj = ((Double) obj).intValue();
		} else {
			i_obj = Integer.parseInt(obj.toString());
		}
		return i_obj;
	}

	public static String getString(Object obj, String charset)
			throws UnsupportedEncodingException {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return new String((byte[]) obj, charset);
		} else {
			return obj.toString();
		}
	}

	/**
	 * 将16进制字符串(带0x和转义字符)转化为字节数组：将十六进制数转化为byte表示<br>
	 * 
	 * 消除字节数组转义字符和0x的字节 如:0x610x620x63\\r\\naa,转换成:97 98 99 13 10 97 97
	 * 
	 * @param str
	 *            源字符串
	 * @return 字节数组
	 * @exception NumberFormatException
	 *                16进制表示不合法
	 */
	public static byte[] escString2Byte(String str)
			throws NumberFormatException {
		byte[] srcData = str.getBytes();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int dataLength = srcData.length;
		for (int i = 0; i < dataLength; i++) {
			if (srcData[i] == CHAR_ZERROR && dataLength >= i + 4
					&& (srcData[i + 1] == 'x' || srcData[i + 1] == 'X')) {
				baos.write((byte) Integer.parseInt(
						new String(srcData, i + 2, 2), 16));
				i += 3;
			} else if (srcData[i] == '\\') {
				if ((i + 1) < dataLength) {
					if (srcData[i + 1] == 'r') {
						baos.write('\r');
					} else if (srcData[i + 1] == 't') {
						baos.write('\t');
					} else if (srcData[i + 1] == 'n') {
						baos.write('\n');
					} else if (srcData[i + 1] == 'b') {
						baos.write('\b');
					} else if (srcData[i + 1] == 'f') {
						baos.write('\f');
					} else if (srcData[i + 1] == '\'') {
						baos.write('\'');
					} else if (srcData[i + 1] == '\"') {
						baos.write('\"');
					} else if (srcData[i + 1] == '\\') {
						baos.write('\\');
					} else if (srcData[i + 1] == '0') {
						baos.write('\0');
					} else {
						throw new IllegalArgumentException("字符串[" + str
								+ "]格式不正确!");
					}
					i++;
				} else {
					// baos.write('\\');
					throw new IllegalArgumentException("字符串[" + str + "]格式不正确!");
				}
			} else {
				baos.write(srcData[i]);
			}
		}
		return baos.toByteArray();
	}

	/**
	 * 输出异常完整堆栈信息
	 * 
	 * @param t
	 * @return
	 */
	public static String printExceptionStackTrace(Throwable t) {
		PrintWriter pw = null;
		StringWriter sw = new StringWriter();
		try {
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		return sw.toString();
	}

	/**
	 * 获得指定长度的由空格重复组成的字符串
	 * 
	 * @param iLen
	 *            所需字符串长度
	 * @return 填充空格后的字符串
	 */
	public static String getBlankStr(int iLen) {
		return getBlankStr(" ", iLen);
	}

	/**
	 * 获得指定长度的由指定字符重复组成的字符串
	 * 
	 * @param cIn
	 *            填充字符
	 * @param iLen
	 *            所需字符串长度
	 * @return 填充指定字符后的字符串
	 */
	public static String getBlankStr(char cIn, int iLen) {
		return getBlankStr(getString(cIn), iLen);
	}

	/**
	 * 获得指定长度的由指定字符串重复组成的字符串 <BR>
	 * 若填充字符串为多字节的话，结果字符串长度可能比需要的要 <b>长 </B>
	 * 
	 * @param sIn
	 *            填充字符串，如果为null或者为空则默认为空格
	 * @param iLen
	 *            所需字符串长度
	 * @return 填充指定字符串后的字符串
	 */
	public static String getBlankStr(String sIn, int iLen) {
		String sBlank = "";

		// 2 row(s) below added by mlrain @2007-8-4 下午05:14:29
		// for: 增加字符串为空的判断
		if (isEmpty(sIn))
			sIn = " ";

		while (sBlank.length() < iLen) {
			sBlank += sIn;
		}
		return sBlank;
	}

	/**
	 * 方向：左
	 */
	public final static int LEFT = 0;

	/**
	 * 方向：居中
	 */
	public final static int CENTER = 1;

	/**
	 * 方向：右
	 */
	public final static int RIGHT = 2;

	/**
	 * 方向：左右
	 */
	public final static int ALL = 3;

	/**
	 * 空字符串
	 */
	public final static String BLANK = "";

	/**
	 * 
	 * <DL>
	 * <DT><B>填充字符串 </B></DT>
	 * <p>
	 * <DD>源字符串右对齐，填充字符为空格</DD>
	 * </DL>
	 * <p>
	 * 
	 * @param source
	 *            源串
	 * @param len
	 *            所需长度
	 * @return 填充后的字符串
	 */
	public static String fillStr(String source, int len) {
		return fillStr(source, " ", len, RIGHT);
	}

	/**
	 * 填充字符串 <BR>
	 * 源串长度大于或等于所需长度则返回原串 <BR>
	 * 源串长度小于所需长度则按对齐方式填充
	 * 
	 * @param source
	 *            源串
	 * @param len
	 *            所需长度
	 * @param align
	 *            对齐方式（0-左，1-中，2-右）
	 * @return 填充后的字符串
	 */
	public static String fillStr(String source, int len, int align) {
		return fillStr(source, " ", len, align);
	}

	/**
	 * 
	 * <DL>
	 * <DT><B>填充字符串 </B></DT>
	 * <p>
	 * <DD>源串右对齐</DD>
	 * </DL>
	 * <p>
	 * 创建时间: 2005-6-16 13:59:08
	 * 
	 * @param source
	 *            源串
	 * @param cIn
	 *            填充字符
	 * @param len
	 *            所需长度
	 * @return 填充后的字符串
	 */
	public static String fillStr(String source, char cIn, int len) {
		return fillStr(source, cIn, len, RIGHT);
	}

	/**
	 * <p>
	 * 填充字符串 <BR>
	 * 源串长度大于或等于所需长度则返回原串 <BR>
	 * 源串长度小于所需长度则按对齐方式填充
	 * </p>
	 * 自定义填充字符 <BR>
	 * 
	 * @param source
	 *            源串
	 * @param cIn
	 *            填充字符
	 * @param len
	 *            所需长度
	 * @param align
	 *            对齐方式（0-左，1-中，2-右）
	 * @return 填充后的字符串
	 */
	public static String fillStr(String source, char cIn, int len, int align) {
		return fillStr(source, getString(cIn), len, align);
	}

	/**
	 * <p>
	 * 填充字符串 <BR>
	 * 源串长度大于或等于所需长度则返回原串 <BR>
	 * 源串长度小于所需长度则按对齐方式填充
	 * </p>
	 * 自定义填充字符串 <BR>
	 * 
	 * @param source
	 *            源串，如果为空则作任何改变
	 * @param sIn
	 *            填充字符串，如果为空则返回源字符串
	 * @param len
	 *            所需长度
	 * @param align
	 *            对齐方式（0-左，1-中，2-右）
	 * @return 填充后的字符串
	 */
	public static String fillStr(String source, String sIn, int len, int align) {
		String sTmp = "";

		// 1 row(s) below edited by mlrain @2007-8-22 下午04:38:08
		// for: 修正以空格无法作为填充字符的BUG；修正源数据不可以为空的BUG
		// 2 row(s) below added by mlrain @2007-8-4 下午05:15:47
		// for: 如果字符串为空则不作任何改变，返回源字串
		if (source == null || sIn == null)
			// if (empty(source) || empty(sIn))
			return source;

		if (source.getBytes().length < len) {
			if (align == LEFT) {
				sTmp = source
						+ getBlankStr(sIn, len - source.getBytes().length);
			} else if (align == CENTER) {
				int iTmp = (len - source.getBytes().length) / 2;
				sTmp = getBlankStr(sIn, iTmp)
						+ source
						+ getBlankStr(sIn, len - source.getBytes().length
								- iTmp);
			} else if (align == RIGHT) {
				sTmp = getBlankStr(sIn, len - source.getBytes().length)
						+ source;
			} else {
				sTmp = source;
			}
		} else {
			sTmp = source;
		}

		return sTmp;
	}

	/**
	 * 
	 * <DL>
	 * <DT><B>split </B></DT>
	 * <p>
	 * <DD>因为 <code>String.split(String)</code> 在jdk1.4之前的版本中没有，所以提供这个方法。使用
	 * <code>StringTokenizer</code> 实现。所以，把空的段都给过滤掉了。如果需要空的段，可以使用
	 * <code>splitEx(String, char)</code> 方法。</DD>
	 * </DL>
	 * <p>
	 * 
	 * @param source
	 *            源字符串
	 * @param s
	 *            分隔符
	 * @return 分割后的字符串数组，如果源数据或者分隔符为空则返回null
	 */
	public static String[] split(String source, String s) {
		// 2 row(s) below added by mlrain @2007-8-4 下午05:17:15
		// for: 如果源数据或者分隔符为空则返回null
		if (isEmpty(source))
			return null;

		// N row(s) below edited by mlrain @2006-12-12 22:08:41
		// for: 使用commons-lang包中的方法，以提高程序效率
		return StringUtils.split(source, s);
		// StringTokenizer st = new StringTokenizer(source, s);
		// String[] ss = new String[st.countTokens()];
		// int i = 0;
		// while (st.hasMoreTokens())
		// {
		// ss[i++] = st.nextToken();
		// }
		// return ss;
	}

	/**
	 * 
	 * <DL>
	 * <DT><B>replace </B></DT>
	 * <p>
	 * <DD>字符串的对字符串的单纯替换操作在jdk1.5之前不提供，故提供之</DD>
	 * </DL>
	 * <p>
	 * 
	 * @param source
	 *            源字符器
	 * @param from
	 *            需要替换的字符串
	 * @param to
	 *            用于替换的字符串
	 * @return 替换后的字符串，如果源数据、需要替换的字符串有一个为null或者为空，或者用于替换的字符串为null则返回源数据
	 */
	public static String replace(String source, String from, String to) {
		// 2 row(s) below added by mlrain @2007-8-4 下午05:18:59
		// for: 如果源数据、需要替换的字符串有一个为null或者为空，或者用于替换的字符串为null则返回源数据
		if (isEmpty(source) || isEmpty(from) || to == null)
			return source;

		// TODO: 这些方法需要优化或者合并
		StringBuffer sb = new StringBuffer(source);
		Stack<String> stack = new Stack<String>();
		int index = source.indexOf(from);
		while (index >= 0) {
			stack.push(String.valueOf(index));
			index = source.indexOf(from, index + from.length());
		}
		while (!stack.empty()) {
			index = Integer.parseInt(stack.pop().toString());
			sb.replace(index, index + from.length(), to);
		}
		return sb.toString();
	}

	// public static void main(String[] args) throws Throwable {
	// long max = 4;
	// String s = "1";
	// for (int i = 0; i < max; i++) {
	// s += "中文";
	// }
	// System.out.println(s);
	// System.out.println(StringTools.toHexTable(s.getBytes()));
	// // System.out.println(System.getProperties());
	// // System.out.println(StringTools.toHexTable("中文".getBytes()));
	// // System.out.println(StringTools.toHexTable(StringTools
	// // .escString2Byte("0x120x11\r\n\\\'\"\t\b\00x39\t0x33")));
	// System.out.println(StringTools.toHexTable(StringTools
	// .escString2Byte("\\00x33\\r\\t")));
	// byte[] b = new byte[] { (byte) 0x96, (byte) 0x87, (byte) 0xE4,
	// (byte) 0xB8, (byte) 0xAD };
	// // System.out.println(b[0] > 0xe0);
	// char c = (char) b[0];
	// System.out.println((byte) c > (byte) 0xe0);
	// String str = "AAAA3F";
	// byte[] b2 = StringTools.string2Byte(str);
	// System.out.println(StringTools.toHexTable(b2));
	// str = "asdfkljjkl;;;;;";
	// System.out.println(StringTools.byte2String(b2));
	// }
	public static String getMsgIdFromBytes(byte[] buff, int pos) {
		if (pos > buff.length - 2 || pos < 0)
			try {
				throw new Exception("Socket buffer Overflow");
			} catch (Exception e) {
				e.printStackTrace();
			}
		byte[] buffer = new byte[2];
		System.arraycopy(buff, 0, buffer, 0, 2);
		return StringTools.toHexString(buffer);
	}
}
