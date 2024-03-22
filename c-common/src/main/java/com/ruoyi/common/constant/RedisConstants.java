package com.ruoyi.common.constant;

/**
 * @ClassName RedisConstants
 * @Description TOD0
 * author axx
 * Date 2023/8/23 9:45
 * Version 1.0
 **/
public class RedisConstants {
    public static final String WECHAT_LOGIN_CODE_KEY = "wechat_login_code:";
    public static final Long LOGIN_CODE_TTL = 5L;
    public static final String WECHAT_LOGIN_USER_KEY = "wechat_login_token:";
    public static final Long LOGIN_USER_TTL = 7L;
    public static final String SEND_SMS_COUNT = "send_sms_count:";


    public static final String PRODUCT_VISIT_COUNT_PRE = "product_visit_count_pre:";
}
