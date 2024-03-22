package com.ruoyi.toc.vo;

import lombok.Data;

@Data
public class StoreInfoVo {

    private Long id;

    private String storeName;

    private String logo;

    private Long fansCount;

    private Long repeatCustomerCount;

    private String entAddress;
}
