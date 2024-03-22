package com.ruoyi.toc.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BasketVo {

    private Long id;


    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 消费者id
     */
    private Long customerId;


    private List<BasketItemVo> basketItemList;

}
