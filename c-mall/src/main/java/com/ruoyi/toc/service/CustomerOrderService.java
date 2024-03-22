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
    void submitOrder(OrderQo orderQo) throws CommonException;

    void cancelOrder(Long id) throws CommonException;

    void unLockSkuStock(Order order) throws CommonException;

    Page<Order> myOrderList(OrderQueryQo orderQueryQo);

    void completeOrder(String orderNo) throws CommonException;

    void deleteOrder(Long id) throws CommonException;

    void updateAddress(OrderAddress orderAddress);

    Object queryOrderDetail(Long id) throws CommonException;

    ConfirmOrderVo purchaseAgain(Long id) throws CommonException;

    void confirmReceive(Long id) throws CommonException;
}
