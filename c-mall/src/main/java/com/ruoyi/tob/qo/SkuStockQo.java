package com.ruoyi.tob.qo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SkuStockQo {

    private Long productSkuId;

    private Long lockQuantity;

    private Long unlockQuantity;

    private String saleOrder;
}
