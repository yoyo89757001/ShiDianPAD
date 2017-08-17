package com.kaeridcard.tools;

import java.nio.charset.Charset;

/**
 * @Author Zhangyu (KAER)
 * @Create 2015 上午9:04:09
 * @Version 1
 * @Description 对byte的相关操作工具类
 *
 */
public class ByteUtils {

	public static byte[] getBytes(short data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		return bytes;
	}

	public static byte[] getBytes(char data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (data);
		bytes[1] = (byte) (data >> 8);
		return bytes;
	}

	public static byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		bytes[2] = (byte) ((data & 0xff0000) >> 16);
		bytes[3] = (byte) ((data & 0xff000000) >> 24);
		return bytes;
	}

	public static byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data >> 8) & 0xff);
		bytes[2] = (byte) ((data >> 16) & 0xff);
		bytes[3] = (byte) ((data >> 24) & 0xff);
		bytes[4] = (byte) ((data >> 32) & 0xff);
		bytes[5] = (byte) ((data >> 40) & 0xff);
		bytes[6] = (byte) ((data >> 48) & 0xff);
		bytes[7] = (byte) ((data >> 56) & 0xff);
		return bytes;
	}

	public static byte[] getBytes(float data) {
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}

	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}

	public static byte[] getBytes(String data, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return data.getBytes(charset);
	}

	public static byte[] getBytes(String data) {
		return getBytes(data, "GBK");
	}

	public static short getShort(byte[] bytes) {
		return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
	}

	public static char getChar(byte[] bytes) {
		return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
	}

	/**
	 * 4BYTE转INT
	 * 
	 * @param bytes
	 * @return
	 */
	public static int getInt(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8))
				| (0xff0000 & (bytes[2] << 16))
				| (0xff000000 & (bytes[3] << 24));
	}

	/**
	 * 2BYTE转INT
	 * 
	 * @param bytes
	 * @return
	 */
	public static int getInt2(byte[] bytes) {
		return (int) ((bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8));
	}

	public static long getLong(byte[] bytes) {
		return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8))
				| (0xff0000L & ((long) bytes[2] << 16))
				| (0xff000000L & ((long) bytes[3] << 24))
				| (0xff00000000L & ((long) bytes[4] << 32))
				| (0xff0000000000L & ((long) bytes[5] << 40))
				| (0xff000000000000L & ((long) bytes[6] << 48))
				| (0xff00000000000000L & ((long) bytes[7] << 56));
	}

	public static float getFloat(byte[] bytes) {
		return Float.intBitsToFloat(getInt(bytes));
	}

	public static double getDouble(byte[] bytes) {
		long l = getLong(bytes);
		return Double.longBitsToDouble(l);
	}

	public static String getString(byte[] bytes, String charsetName) {
		return new String(bytes, Charset.forName(charsetName));
	}

	public static String getString(byte[] bytes) {
		return getString(bytes, "GBK");
	}

	/**
	 * 返回空的byte数组(用于保留)
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] getVoidBytes(int num) {
		byte[] bytes = new byte[num];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) 0xff;
		}
		return bytes;
	}

	/**
	 * 将空位填
	 * 
	 * @param bytes
	 *            要填充的数组
	 * @param size
	 *            数组填充后的大小
	 * @return
	 */
	public static byte[] fillZero(byte[] bytes, int size) {
		if (bytes.length >= size) {
			return bytes;
		}
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i < bytes.length) {
				data[i] = bytes[i];
			} else {
				data[i] = (byte) 0x00;
			}
		}
		return data;
	}

	/**
	 * 16进制输出BYTE数组
	 * 
	 * @param data
	 * @return
	 */
	public static String formatData(byte[] data) {
		String hex_str = Base16Utils.encode(data);
		String regex = "(.{2})";
		hex_str = hex_str.replaceAll(regex, "$1 ");
		return hex_str;
	}

	/**
	 * 将指定byte数组转换16进制大写字符
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

}
