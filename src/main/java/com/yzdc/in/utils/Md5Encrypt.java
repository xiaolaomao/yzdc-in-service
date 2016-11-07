package com.yzdc.in.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Desc:    MD5加密类
 * Author: Iron
 * CreateDate: 2016-10-12
 * CopyRight: Beijing Yunzong Co., Ltd.
 */
public class Md5Encrypt {
    private static final Logger logger = LogManager.getLogger(Md5Encrypt.class);

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }
        msgDigest.update(text.getBytes());
        byte[] bytes = msgDigest.digest();
        String md5Str = new String(encodeHex(bytes));
        return md5Str;
    }

    /**
     * MD5解密
     *
     * @param data
     * @return
     */
    public static char[] encodeHex(byte[] data) {
        int len = data.length;
        char[] out = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }

}
