package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 微信小程序普通用户令牌前缀
     */
    public static final String WECHAT_LOGIN_USER_KEY = "wechat_login_user_key";


    /**
     * 微信小程序销售用户令牌前缀
     */
    public static final String WECHAT_SALES_LOGIN_USER_KEY = "wechat_sales_login_user_key";


    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "com.ruoyi" };

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file", "com.ruoyi.common.config" };

    /**
     * RSA私钥
     */
    public static final String PRI = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDd8+anh3e4ejFUfhW0C5atD14EeYVdS5naZYm+usFO3Du4ipJonZvjM1HJZb9KTe09C8N+YnkA4IDnEXjq9mqeuQr2ZIXDH4Ngx6w9MQNyFW42nUIQN07FM6iU0tWpCfY/7oO5+pucKyPrWJlrk4sDiGshwAu/NBOGF1xoKs6yAMH/6kKaW2LDCQB8oIBmtwDVLV2qCAnHoPCQrn5rzfNaiOQIaSH9DmxHiWUrYqDeHVR5jdB5vco8C2ez45r/TgY3ZkgXMBa10Too3BRo+1lynOuowNYlQU3ULEGEOe3zmBROi7+geeUAZnWGc94CV5dbTXmM9nGgLMBAyzpPIrUvAgMBAAECggEAcRn8i5ook8UQpjGp9ZmCKqYODtGt+CCyQexECQE5yGnJtPxIJy9JZLmeD7bdh4u2PMll0V10s9GzXgw1ReZT5pX81BndX9ULKaIWTwhO7S+mfgYcyA6nAS6V1WCqp/z/kwyD652c9jO1lhQL+hmxWReHICew1LpAyK/W0u4dbe6p+f5CYygm06LzspcRfSNxEZDX9Jf4PNUGT48Ul9sVma+gtiP+tIzhTLH1qOKwCLUIQ7f8U8SyyvkcHI6oNwmIXLllOEVrxTW8R2ZGfFVB8Hvoz5OhJbPlCwujyJZxSBKANb2L5RH8N3Edf/2qpMwzaZHKaCZeWtV5dVkOSb7pAQKBgQDwyohM63nSoL90FTu1RIOZntZY3B9dP/xrX+QP5cf69lD9JUbWerWd7vSZ+XdXiwKxAO4qQJ+EufzsnVexlggvANHPQumF4F6PMN9Djg0Ws0Eg+iiR8tUoVcWnaVfAkIV4nmx2jU/eFpjGAWYvtLK7u+1Ruv7netGGcBJbqPv7YQKBgQDr+MLpQQdyTRQMOTHsjdGK7RqCobHAVOMESBVejn0VFeW1Wp6nrRAPjYKXyHf1UH1VTp1B3ftbre9lbXvWuZBqh0zr+/aRPi3Rx06MFxebWS1rmhvk8xDnp+mR4sEb4+MMdldDKum/tFFvPjL6DRPyLS4F1Nfge12VqHlPOD+KjwKBgQDe7ILXw8HbMJtPW0EWBsJXfOgxCEm5ST3mIra/yRoly9kbJGG6u4xdFRc7XewRDk+Fk7jrQTzt4kq4vMqtTJty+K1F10rfeOPSaYqpvl97vqTzZOYeiNSzLQV6glqNmr8W9oSwFQ/2KrFxz2XNkNtQVyHVYuwPXnOQpT05wu7WwQKBgDn2Vto40P1wlXYTLoIuq8P2Tca8tQ7LSyAoHKenVG4zfMM5STNivUk0t1lVMrIdXUvxDYu6XR298cn2RK8P0bt5FJn6wCwZG+QlktOycGgGVbPSnPt2ATcF9ceR7WFObt0GzJSBG/2s6zvqCVeGktmv41gnWgeYBfrW8UVWSbVdAoGAA9NOpw6yecqAhaNfzs/77h2tDFgWMsNdAaGFAoFMUJdsxheRbDUkmLsOzQ/PHoOal4MPykhtmC0VHG17byWlwqeJuOOVrMKKjf5ZjYDoAmuYnOryCrfdQj6/wa1vdrXjh/Z+Hbgt5lROgnZAtRQNsK6NHRK0E/WWBcvO917aspA=";
    /**
     * RSA公钥
     */
    public static final String PUB = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3fPmp4d3uHoxVH4VtAuWrQ9eBHmFXUuZ2mWJvrrBTtw7uIqSaJ2b4zNRyWW/Sk3tPQvDfmJ5AOCA5xF46vZqnrkK9mSFwx+DYMesPTEDchVuNp1CEDdOxTOolNLVqQn2P+6DufqbnCsj61iZa5OLA4hrIcALvzQThhdcaCrOsgDB/+pCmltiwwkAfKCAZrcA1S1dqggJx6DwkK5+a83zWojkCGkh/Q5sR4llK2Kg3h1UeY3Qeb3KPAtns+Oa/04GN2ZIFzAWtdE6KNwUaPtZcpzrqMDWJUFN1CxBhDnt85gUTou/oHnlAGZ1hnPeAleXW015jPZxoCzAQMs6TyK1LwIDAQAB";

    /**
     *  下架原因
     */
    public static final String MEAL_OFF_SHELF_REASON = "meal_off_shelf_reason";
    /**
     * 机构证件类型
     */
    public static final String CERTYFICATE_TYPE = "certificate_type";
    /**
     *  强制下架
     */
    public static final String FORCED_REMOVAL = "forced_removal";
    /**
     * 更新套餐
     */
    public static final String UPDATE_MEAL = "update_meal";
    /**
     * 审核通过
     */
    public static final String APPROVED = "approved";
    /**
     * 驳回
     */
    public static final String REJECTED = "rejected";
    /**
     * 待审核
     */
    public static final String PENDING_APPROVAL = "pending_approval";
    /**
     * 机构标签
     */
    public static final String INSTITUTION_LAB = "institution_lab";
    /**
     *  机构服务对象
     */
    public static final String SERVICE_TARGET = "service_target";

    /**
     *  机构基础服务
     */
    public static final String BASE_SERVICE = "base_service";

    /**
     *  机构室内配套
     */
    public static final String INDOOR_MATING = "indoor_mating";



    /**
     *  机构社区配套
     */
    public static final String COMMUNITY_MATING = "community_mating";

    /**
     *  机构医疗配套
     */
    public static final String MEDICAL_MATING = "medical_mating";

    /**
     *  机构医疗服务
     */
    public static final String MEDICAL_SERVICE = "medical_service";


    /**
     *  机构星级
     */
    public static final String INSTITUTION_STAR_RATING = "institution_star_rating";

    /**
     *  客群
     */
    public static final String CUSTOMER_GROUP = "customer_group";

    public static final String WECHAT_ACCESS_TOKEN_KEY = "wechat_access_token_KEY";


    public static final String CUS_RELATION_SHIP = "cus_relation_ship";

    public static final String CUS_PHYSICAL_CONDITION = "cus_physical_condition";

    public static final String SYS_USER_SEX = "sys_user_sex";

    /**
     *  销售专员
     */
    public static final String SALES_SPECIALIST = "sales-specialist";


    /**
     *  销售主管
     */
    public static final String SALES_EXECUTIVE = "sales-executive";

    public static final String SALES_TASK_LAB = "sales_task_lab";

    /**
     *  客户节点状态-获客
     */
    public static final String ACQUIRING_CUSTOMERS = "acquiring_customers";

    /**
     *  客户节点状态-跟进
     */
    public static final String FOLLOW_UP = "follow_up";

    /**
     *  客户节点状态-预约
     */
    public static final String RESERVATION = "reservation";

    /**
     *  客户节点状态-成交
     */
    public static final String TRADED = "traded";

    /**
     *  微信用户默认头像
     */
    public static final String DEFAULT_AVATAR = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAASFBMVEX09Pby8vTe3uDPz9DJycm/v8C9vb3BwcLLy8vR0dLp6evn5+i1tbWysrLb29zu7vDNzc7r6+26urrg4OG3t7fj4+TFxcbV1dY8QE/jAAABo0lEQVRo3u2Yi3KCMBBFCY8EIWs0Iv7/n7Y4djqFZEXCTVtnzw+cYZfsqygEQRAEQRAEQRCEt0OVVd1o0x66rNq+tvRFc8ymdSf6gTnn8XaeZthLDm8/0JIM5s5SiCvaq3TQS96BxRVFaLHeM0XBPuhTXAz9ZGfjYkJm+ch4CVnBak5cA8UDJx5wXkUsCiY+82Jcr+h5cf9+4o4X42qX48XACsIVLrI4b6E5sQaKW06M7BIlJy6BYsUk2eIKV8EMIEQV0lu4aJsYwENXtCPD9wkT9hq0NzJX2wy72/FXAj0xLr1jDm9g1LToNeLBYS4+5PEu23Kuq8Bi5oMWy78gvs7F8OX4QTMXN3m8gY3xlEHrgvtTDX/JF09BPPb80o0UZQSO1cw94J5pTLzVzdIT7A3wokv/THtP9d6jZm/WaCfMnquba9dqJ9rdUv08ufNU7xPl5jXtRLNDvG+vaydSP1qN27yf9STpZanVP/MSk2LeGOf0aG/4r75J6dKrqlUML+L/INYp4pQrUJUiTrlNqEr7jegq15gvCIIgCIIgCIIg7M4HkS5rj/A3dbUAAAAASUVORK5CYII=";

    /**
     *  推广类型
     */
    public static final String PROMOTE = "promote";


    /**
     *  带看类型
     */
    public static final String GUIDED_TOUR = "guided-tour";

    /**
     *  任务
     */
    public static final String TASK = "task";

    /**
     *  客户跟进
     */
    public static final String CUSTOM_FOLLOW_UP = "custom_follow_up";

    /**
     *  机构跟进
     */
    public static final String INSTITUTION_FOLLOW_UP = "institution_follow_up";

    public static final String TRANSACTION = "transaction";

    public static final String DISABLE = "disable";
    public static final String ENABLE = "enable";

    public static final String ZERO = "0";

    public static final String ONE = "1";

    public static final String TWO = "2";

    public static final String THREE = "3";


    public static final String MALL_ORDER_STATUS_1 = "waiting_pay";
    public static final String MALL_ORDER_STATUS_2 = "waiting_delivery";
    public static final String MALL_ORDER_STATUS_3 = "delivered";
    public static final String MALL_ORDER_STATUS_4 = "completed";
    public static final String MALL_ORDER_STATUS_5 = "closed";
}
