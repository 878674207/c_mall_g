package com.ruoyi.toc.qo;

import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderQueryQo extends BaseQo<Order> {
    private String orderStatus;

    private List<Long> orderIdList;
}
