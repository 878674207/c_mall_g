package com.ruoyi.toc.qo;

import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.PurchaseOrder;
import lombok.Data;

import java.util.Set;

@Data
public class PurchaseOrderQo extends BaseQo<PurchaseOrder> {

    private String productName;

    private Set<Long> purchaseOrderIds;

}
