package com.ruoyi.toc.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.delayqueue.handle.RedisDelayQueueHandle;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.mapper.OrderMapper;
import com.ruoyi.toc.service.CustomerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class MallOrderPaymentTimeoutHandler implements RedisDelayQueueHandle<String> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CustomerOrderService customerOrderService;

    @Override
    public void execute(String orderNo) {
        log.info("(收到订单支付超时延迟消息)订单号为 {}", orderNo);

        // 已经手动取消的订单无需处理
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_no", orderNo).last("limit 1"));
        if (Objects.isNull(order) || !Constants.MALL_ORDER_STATUS_1.equals(order.getOrderStatus())) {
            return;
        }

        // 解锁库存
        try {
            customerOrderService.unLockSkuStock(order);
        } catch (CommonException e) {
            log.error("order cancel fail,orderNo is {},faild reason is {}", orderNo, e);
        }

        // todo 后续接入支付，还需要取消支付那边的订单

        // 更新订单状态
        orderMapper.updateById(new Order().setId(order.getId()).setOrderStatus(Constants.MALL_ORDER_STATUS_5));
    }
}
