package com.ruoyi.toc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class PaymentVo {

    // 合计 = 运费 + 商品总价格
    private BigDecimal totalPrice;

    // 运费
    private BigDecimal deliverPrice;

    // 商品总价格
    private BigDecimal productTotalPrice;
}
