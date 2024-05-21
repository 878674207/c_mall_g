package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.converter.OrderConverter;
import com.ruoyi.tob.entity.OrderRemarkLog;
import com.ruoyi.tob.mapper.OrderRemarkLogMapper;
import com.ruoyi.tob.qo.OrderQo;
import com.ruoyi.tob.service.OrderService;
import com.ruoyi.tob.vo.OrderOperationTime;
import com.ruoyi.tob.vo.OrderVo;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.entity.OrderItem;
import com.ruoyi.toc.mapper.OrderAddressMapper;
import com.ruoyi.toc.mapper.OrderItemMapper;
import com.ruoyi.toc.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private OrderRemarkLogMapper orderRemarkLogMapper;

    @Override
    public List<OrderVo> queyOrderList(OrderQo orderQo) {
        if (StringUtils.isNotBlank(orderQo.getProductName())) {
            List<OrderItem> orderItemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                    .like(OrderItem::getProductName, orderQo.getProductName()));
            if (CollectionUtils.isEmpty(orderItemList)) {
                return Lists.newArrayList();
            }
            orderQo.setOrderIdList(orderItemList.stream().map(OrderItem::getOrderId).collect(Collectors.toList()));
        }

        orderQo.startPage();
        List<OrderVo> orderVos = orderMapper.queyOrderList(orderQo);
        buildOrderItem(orderVos);
        return orderVos;
    }

    @Override
    public OrderVo queryOrderDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        OrderVo orderVo = OrderConverter.INSTANCE.toOrderVo(order);
        buildOrderOperationTime(orderVo);
        orderVo.setOrderItemList(orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id)));
        // 收货信息
        orderVo.setOrderAddress(orderAddressMapper.selectOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, id).last("limit 1")));
        orderVo.setOrderRemarkLogList(orderRemarkLogMapper.selectList(new LambdaQueryWrapper<OrderRemarkLog>().eq(OrderRemarkLog::getOrderId, id)));
        return orderVo;
    }

    private void buildOrderOperationTime(OrderVo orderVo) {
        List<OrderOperationTime> orderOperationTimeList = Lists.newArrayList();
        if (Objects.nonNull(orderVo.getCreateTime())) {
            orderOperationTimeList.add(new OrderOperationTime().setOrderNode("create-order").setOperationTime(orderVo.getCreateTime()));
        }
        if (Objects.nonNull(orderVo.getPaymentTime())) {
            orderOperationTimeList.add(new OrderOperationTime().setOrderNode("payment-order").setOperationTime(orderVo.getPaymentTime()));
        }
        if (Objects.nonNull(orderVo.getDeliveryTime())) {
            orderOperationTimeList.add(new OrderOperationTime().setOrderNode("delivery-goods").setOperationTime(orderVo.getDeliveryTime()));
        }
        if (Objects.nonNull(orderVo.getReceiveTime())) {
            orderOperationTimeList.add(new OrderOperationTime().setOrderNode("receive-goods").setOperationTime(orderVo.getReceiveTime()));
        }
        if (Objects.nonNull(orderVo.getCommentTime())) {
            orderOperationTimeList.add(new OrderOperationTime().setOrderNode("comment-order").setOperationTime(orderVo.getCommentTime()));
        }
        orderVo.setOrderOperationTimeList(orderOperationTimeList);
    }

    @Override
    public void addOrderRemark(OrderRemarkLog orderRemarkLog) {
        Order order = orderMapper.selectById(orderRemarkLog.getOrderId());
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        orderRemarkLog.setOrderStatus(order.getOrderStatus());
        orderRemarkLog.setCreateTime(DateUtils.getNowDate());
        orderRemarkLog.setCreateBy(SecurityUtils.getUsername());
        orderRemarkLogMapper.insert(orderRemarkLog);
    }

    @Override
    public void confirmDelivery(Long id) {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order)) {
            throw new CommonException("订单不存在!");
        }
        orderMapper.updateById(new Order().setId(id).setDeliveryTime(DateUtils.getNowDate()).setOrderStatus(Constants.MALL_ORDER_STATUS_3));
    }

    private void buildOrderItem(List<OrderVo> orderVos) {
        if (CollectionUtils.isEmpty(orderVos)) {
            return;
        }
        List<Long> orderIds = orderVos.stream().map(OrderVo::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        List<OrderItem> orderItemList = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().in(OrderItem::getOrderId, orderIds));
        if (CollectionUtils.isEmpty(orderItemList)) {
            return;
        }
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
        orderVos.forEach(item -> item.setOrderItemList(orderItemMap.get(item.getId())));
    }
}
