package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.toc.entity.PurchaseOrder;
import com.ruoyi.toc.qo.PurchaseOrderItemQo;
import com.ruoyi.toc.qo.PurchaseOrderQo;

import java.util.List;

public interface PurchaseOrderService {
    void savePurchaseOrder(PurchaseOrder purchaseOrder);

    void deletePurchaseOrder(Long id) throws CommonException;

    Page<PurchaseOrder> queryPurchaseOrderList(PurchaseOrderQo purchaseOrderQo);

    List<PurchaseOrder> queryAllPurchaseOrderList();

    PurchaseOrder queryHomePagePurchaseOrder();

    void addProduct(PurchaseOrderItemQo purchaseOrderItemQo) throws CommonException;

    void updateProduct(PurchaseOrderItemQo purchaseOrderItemQo);
}
