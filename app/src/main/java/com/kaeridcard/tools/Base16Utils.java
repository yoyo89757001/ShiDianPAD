package com.kaeridcard.tools;

/**
 * 16进制编解
 * 
 * @author Wxcily
 *
 */
public class Base16Utils {

	// 编码字符
	public static final char[] ENC_TAB = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	// 解码字符
	public static final byte[] DEC_TAB = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00, // 16
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00, // 32
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00, // 48
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00,
			0x00,
			0x00,
			0x00,
			0x00,
			0x00, // 64
			0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00,
			0x00,
			0x00,
			0x00, // 80
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00,
			0x00,
			0x00, // 96
			0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00,
			0x00, // 112
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * 将字节数组数据编码为16进制字符
	 * 
	 * @param data
	 *            byte数组
	 * @return base
	 */
	public static String encode(byte[] data) {
		return encode(data, 0, data.length);
	}

	/**
	 * 将字节数组数据编码为16进制字符
	 * 
	 * @param data
	 *            byte数组
	 * @param offset
	 *            偏移
	 * @param length
	 *            编码长度
	 * @return 16进制字符
	 */
	public static String encode(byte[] data, int offset, int length) {
		StringBuffer buff = new StringBuffer(length * 2);
		int i = offset, total = offset + length;
		while (i < total) {
			buff.append(ENC_TAB[(data[i] & 0xF0) >> 4]);
			buff.append(ENC_TAB[data[i] & 0x0F]);
			i++;
		}

		return buff.toString();
	}

	/**
	 * 解码16进制字符串为byte数组
	 * 
	 * @param hex
	 *            16进制字符
	 * @return byte byte数组
	 */
	public static byte[] decode(String hex) {
		byte[] data = new byte[hex.length() / 2];
		decode(hex, data, 0);
		return data;
	}

	private static void decode(String hex, byte[] data, int offset) {
		int i = 0, total = (hex.length() / 2) * 2, idx = offset;
		while (i < total) {
			data[idx++] = (byte) ((DEC_TAB[hex.charAt(i++)] << 4) | DEC_TAB[hex
					.charAt(i++)]);
		}
	}
}
