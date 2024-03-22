package com.ruoyi.toc.qo;

import lombok.Data;

@Data
public class SettleQo {

    private Long productId;

    private Long productSkuId;


    private Long quantity;

    private Long deliveryAddressId;
}
