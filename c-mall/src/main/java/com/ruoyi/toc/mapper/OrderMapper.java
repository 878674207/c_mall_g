package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.qo.OrderQo;
import com.ruoyi.tob.vo.OrderVo;
import com.ruoyi.toc.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    Long queryStoreRepeatCustomerCount(Long storeId);

    List<OrderVo> queyOrderList(@Param("orderQo") OrderQo orderQo);

}
