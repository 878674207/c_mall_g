package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.toc.entity.OrderItem;
import com.ruoyi.toc.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {
}
