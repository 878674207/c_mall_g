package com.ruoyi.toc.qo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class BasketQo {

    private Long customerId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;


    /**
     * 购物车id
     */
    private Long basketId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * sku表id
     */
    private Long productSkuId;

    /**
     * 购买数量
     */
    private Long quantity;

    private List<Long> basketItemIds;
}
