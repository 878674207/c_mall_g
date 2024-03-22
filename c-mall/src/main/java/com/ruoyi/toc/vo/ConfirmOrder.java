package com.ruoyi.toc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ConfirmOrder {

    private Long id;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    private String remark;


    private List<ConfirmOrderItem> confirmOrderItems;

}
