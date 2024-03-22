package com.ruoyi.tob.qo;

import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQo extends BaseQo<Order> {

    private String orderNo;

    private String receiverInfo;

    private String startTime;

    private String endTime;

    private String productName;

    private Long storeId;

    private List<Long> orderIdList;

    private String orderStatus;

}


