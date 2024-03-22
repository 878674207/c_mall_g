package com.ruoyi.common.utils.security;

import com.ruoyi.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author wangj
 * @description RSA加解密工具类
 * @date 2023/6/1 15:44
 */
@Slf4j
public class RsaUtil {

    public static void main(String[] args) throws Exception {
        RsaUtil.getKeyPair();
    }

    /**
     * 获取密钥对
     *
     * @return 密钥对
     * @throws Exception {@link Exception}
     */
    public static KeyPair getKeyPair() throws Exception {
        // 获取RSA算法的密钥生成器对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 设置密钥长度2048位
        keyPairGenerator.initialize(2048);
        // 生成密钥对对象
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        log.info("pub: {}", publicKeyStr);
        log.info("pri: {}", privateKeyStr);

        return keyPair;
    }

    /**
     * RSA公钥加密
     *
     * @param str 加密字符砖
     * @return 密文
     * @throws Exception {@link Exception}
     */
    public static String encrypt(String str) throws Exception {
        // base64编码公钥
        byte[] decoded = Base64.decodeBase64(Constants.PUB);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));

        return outStr;
    }

    public static String decrypt(String str) throws Exception {
        // base64解码密文
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码私钥
        byte[] decoded = Base64.decodeBase64(Constants.PRI);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));

        return outStr;
    }
}
