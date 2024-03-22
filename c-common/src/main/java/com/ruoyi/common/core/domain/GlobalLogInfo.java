package com.ruoyi.common.core.domain;

import lombok.Data;

@Data
public class GlobalLogInfo {


    private String username;

    /**
     *  请求的当前时间
     */
    private Long curTime;


    /**
     *  接口域名
     */
    private String basePath;

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 请求的方法名
     */
    private String methodName;

}
