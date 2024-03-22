package com.ruoyi.toc.vo;

import com.ruoyi.toc.entity.DeliveryAddress;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ConfirmOrderVo {

    private DeliveryAddress orderAddress;

    private List<ConfirmOrder> confirmOrderList;

    private PaymentVo paymentVo;
}
