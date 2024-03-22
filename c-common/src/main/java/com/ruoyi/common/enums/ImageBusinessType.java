package com.ruoyi.common.enums;

/**
 * @ClassName BusinessType
 * @Description TOD0
 * author axx
 * Date 2023/9/6 16:54
 * Version 1.0
 **/
public enum ImageBusinessType {
    /**
     * 养老机构
     */
    INSTITUTION("1"),
    /**
     * 资讯信息
     */
    INFO("2"),
    /**
     * 套餐小图
     */
    MEAL_MAIN_IMAGE("meal_main_image"),

    /**
     * 套餐详情图
     */
    MEAL_DETAIL_IMAGE("meal_detail_image"),

    /**
     * 机构logo图
     */
    INSTITUTION_LOGO("institution_logo"),
    /**
     * 机构营业执照图
     */
    INSTITUTION_BUSINESS_LICENSE("institution_business_license"),

    /**
     * 机构详情图
     */
    INSTITUTION_DETAIL_IMAGE("institution_detail_image"),


    /**
     * 跟进记录图
     */
    FOLLOW_UP_IMAGE("follow_up_image"),


    /**
     * 跟进记录图
     */
    COMPLAINT_UP_IMAGE("complaint_up_image"),


    /**
     * 到访记录图
     */
    VISIT_RECORD_IMAGE("visit_record_image"),


    /**
     * 个人任务回复图片
     */
    PERSONAL_TASK_REPLY_IMAGE("personal_task_reply_image"),


    /**
     * 成交合同凭证
     */
    TRADED_CONTRACT_ACCESS_IMAGE("traded_contract_access_image"),


    /**
     * 商品轮播图
     */
    PRODUCT_CAROUSEL_IMAGE("product_carousel_image"),

    EVALUATION("4");

    private String code;

    ImageBusinessType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
