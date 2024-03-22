package com.ruoyi.common.utils;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author wangj
 * @description 配置文件加密工具类
 * @date 2023/7/17 15:26
 */
public class JaspytUtil {

    /**
     * 加密
     *
     * @param str
     *            原文
     * @param password
     *            密钥
     * @return 密文
     */
    public static String encrypt(String str, String password) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        // 密钥
        encryptor.setPassword(password);
        // 加密
        String encStr = encryptor.encrypt(str);

        return encStr;
    }

    /**
     * 解密
     *
     * @param str
     *            密文
     * @param password
     *            密钥
     * @return 原文
     */
    public static String decrypt(String str, String password) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        // 密钥
        encryptor.setPassword(password);
        // 加密
        String decStr = encryptor.decrypt(str);

        return decStr;
    }

}
