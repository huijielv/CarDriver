package com.ymx.driver.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by wuwei
 * 2018/8/28
 * 佛祖保佑       永无BUG
 */
public class Encrypt3DESUtil {
    private static final String ENCRYPTION_MANNER = "DESede";
    private static final String ENCRYPTION_KEY = "DESede";


    /**
     * 加密
     *
     * @param data 加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(byte[] data) throws Exception {
        // 恢复密钥
        SecretKey secretKey = new SecretKeySpec(build3Deskey(new StringBuilder()
                .toString().getBytes()), ENCRYPTION_MANNER);
        // Cipher完成加密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MANNER);
        // cipher初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypt = cipher.doFinal(data);
        //转码
        return new String(Base64.encode(encrypt, Base64.DEFAULT), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param data 加密后的字符串
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String data) throws Exception {
        // 恢复密钥
        SecretKey secretKey = new SecretKeySpec(build3Deskey(new StringBuilder()
                .toString().getBytes()), ENCRYPTION_MANNER);
        // Cipher完成解密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MANNER);
        // 初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        //转码
        byte[] bytes = Base64.decode(data.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        //解密
        byte[] plain = cipher.doFinal(bytes);
        //解密结果转码
        return new String(plain, StandardCharsets.UTF_8);
    }

    public static byte[] build3Deskey(byte[] temp) throws Exception {
        byte[] key = new byte[24];
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
