package com.ruoyi.tob.service;

import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.entity.OrderRemarkLog;
import com.ruoyi.tob.qo.OrderQo;
import com.ruoyi.tob.vo.OrderVo;

import java.util.List;

public interface OrderService {
    List<OrderVo> queyOrderList(OrderQo orderQo);

    OrderVo queryOrderDetail(Long id);

    void addOrderRemark(OrderRemarkLog orderRemarkLog);

    void confirmDelivery(Long id);
}
