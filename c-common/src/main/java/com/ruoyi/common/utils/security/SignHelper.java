package com.ruoyi.common.utils.security;


import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class SignHelper {



    public static final String KEY_ALGORTHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";

    public static final String SIGNATURE_PARAM_KEY = "signature";

    public static final MapJoiner PARAM_MAP_JOINER = Joiner.on("&").withKeyValueSeparator("=")
            .useForNull(StringUtils.EMPTY);

    private static final Joiner PARAM_ARRAY_JOINER = Joiner.on(",").skipNulls();

    /**
     * 用私钥对信息生成数字签名
     *
     * @param bizContent 数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String bizContent, String privateKey) throws Exception {
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // byte[] keyBytes = privateKey.getBytes();

        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(bizContent.getBytes());

        return encryptBASE64(signature.sign());
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param bizParams  Map参数
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String signStringParam(Map<String, String> bizParams,  String privateKey) throws Exception {
        TreeMap<String, String> bizSortParam = new TreeMap<>();
        bizSortParam.putAll(bizParams);
        String bizContent = PARAM_MAP_JOINER.join(bizSortParam);
        return sign(bizContent, privateKey);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param bizParams  Map参数
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String signArrayParam(Map<String, String[]> bizParams,  String privateKey) throws Exception {
        TreeMap<String, String> bizSortParam = new TreeMap<>();

        for (Entry<String, String[]> entry : bizParams.entrySet()) {
            String paramKey = entry.getKey();
            String[] paramValue = entry.getValue();

            if (ArrayUtils.isNotEmpty(paramValue)) {
                bizSortParam.put(paramKey, PARAM_ARRAY_JOINER.join(paramValue));
            } else {
                bizSortParam.put(paramKey, StringUtils.EMPTY);
            }
        }
        String bizContent = PARAM_MAP_JOINER.join(bizSortParam);
        return sign(bizContent, privateKey);
    }

    /**
     * 校验数字签名
     *
     * @param bizContent 数据
     * @param publicKey  公钥
     * @param sign       数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String bizContent, String publicKey,
                                 String sign) throws Exception {

        if (StringUtils.isBlank(publicKey) || StringUtils.isBlank(sign)) {
            return false;
        }

        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(bizContent.getBytes());
        //验证签名是否正常
        try {
            return signature.verify(decryptBASE64(sign));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验数字签名
     *
     * @param bizParams map数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifyStringParam(Map<String, String> bizParams, String publicKey,
                                            String sign) throws Exception {
        TreeMap<String, String> bizSortParam = new TreeMap<>();
        bizSortParam.putAll(bizParams);
        String bizContent = PARAM_MAP_JOINER.join(bizSortParam);
        return verify(bizContent, publicKey, sign);
    }

    /**
     * 校验数字签名
     *
     * @param bizParams map数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifyArrayParam(Map<String, String[]> bizParams, String publicKey,
                                           String sign) throws Exception {
        TreeMap<String, String> bizSortParam = new TreeMap<>();

        for (Entry<String, String[]> entry : bizParams.entrySet()) {
            String paramKey = entry.getKey();
            String[] paramValue = entry.getValue();

            if (ArrayUtils.isNotEmpty(paramValue)) {
                bizSortParam.put(paramKey, PARAM_ARRAY_JOINER.join(paramValue));
            } else {
                bizSortParam.put(paramKey, StringUtils.EMPTY);
            }
        }

        String bizContent = PARAM_MAP_JOINER.join(bizSortParam);
        return verify(bizContent, publicKey, sign);
    }


    /** * BASE64解密 * * @param key = 需要解密的密码字符串 * @return * @throws Exception */
    public static byte[] decryptBASE64(String key) throws Exception {


        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /** * BASE64加密 * * @param key = 需要加密的字符数组 * @return * @throws Exception */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }






}