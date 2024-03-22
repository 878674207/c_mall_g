package com.ruoyi.tob.converter;

import com.ruoyi.tob.vo.OrderVo;
import com.ruoyi.toc.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderConverter {

    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    OrderVo toOrderVo(Order order);

}
