package com.ruoyi.toc.qo;

import com.ruoyi.toc.vo.ConfirmOrder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderQo {

    @NotNull(message = "请先完善收货地址信息")
    private OrderAddressQo orderAddress;

    /**
     *  结算来源  basket  ->从购物车结算        product-detail  ->从商品详情结算
     */
    private String settleFrom;


    private List<ConfirmOrder> confirmOrderList;

}
