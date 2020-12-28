package com.ymx.driver.util;

import java.security.MessageDigest;

public class MD5 {

	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
            byte[] bytes = md.digest();

			return NumberConvertUtils.byteToHex(bytes).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 使用指定Key进行MD5加密
	 *
	 * @param key
	 * @param content
	 * @return
	 */
	public static String encryption(String key, String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes());
			byte[] bytes = md.digest(key.getBytes());
			return NumberConvertUtils.byteToHex(bytes).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
