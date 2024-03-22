package com.ruoyi.toc.vo;

import com.ruoyi.toc.entity.DeliveryAddress;
import lombok.Data;

@Data
public class DeliveryVo {

    private String deliveryFrom;

    private DeliveryAddress deliveryTo;

}
