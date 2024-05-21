package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.qo.OrderQo;
import com.ruoyi.toc.qo.OrderQueryQo;
import com.ruoyi.toc.vo.ConfirmOrderVo;

public interface CustomerOrderService {
    void submitOrder(OrderQo orderQo);

    void cancelOrder(Long id);

    void unLockSkuStock(Order order);

    Page<Order> myOrderList(OrderQueryQo orderQueryQo);

    void completeOrder(String orderNo);

    void deleteOrder(Long id);

    void updateAddress(OrderAddress orderAddress);

    Object queryOrderDetail(Long id);

    ConfirmOrderVo purchaseAgain(Long id);

    void confirmReceive(Long id);
}
