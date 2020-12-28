package com.ymx.driver.util;

/**
 * Decripe : 进制转化 
 * Date : 2016年3月5日 Version : 1.0 <br/>
 * 
 * @author : wangkeze
 */
public class NumberConvertUtils {

	public static final String HEX_STRING = "0123456789ABCDEF";

	/**
	 * byte制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes) {
		if (null == bytes || bytes.length == 0)
			return null;

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int num = bytes[i] & 0xFF;
			String temp = Integer.toHexString(num);
			if (temp.length() < 2) {
				hex.append("0");
			}
			hex.append(temp.toUpperCase());
		}

		return hex.toString();
	}

	/**
	 * 十六进制转byte
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByte(String hex) {
		int length = hex.length();
		byte[] bytes = new byte[length / 2];
		char[] charArrays = hex.toCharArray();
		for (int i = 0; i < length; i += 2) {
			bytes[i / 2] = (byte) (charToByte(charArrays[i]) << 4 | charToByte(charArrays[i + 1]));
		}
		return bytes;
	}

	public static byte charToByte(char c) {
		return (byte) HEX_STRING.indexOf(c);
	}

}
