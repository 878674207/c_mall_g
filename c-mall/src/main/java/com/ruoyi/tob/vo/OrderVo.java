package com.ruoyi.tob.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.tob.entity.OrderRemarkLog;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderVo {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 消费者id
     */
    private Long customerId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 应付金额（实际支付金额）
     */
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 促销优化金额（促销价、满减、阶梯价）
     */
    private BigDecimal promotionAmount;

    /**
     * 订单状态：waiting_pay->待付款；waiting_delivery->待发货；delivered->已发货；completed->已完成；closed->已关闭；
     */
    private String orderStatus;

    /**
     * 确认收货状态：0->未确认；1->已确认
     */
    private Integer confirmStatus;

    /**
     * c端删除状态：0->未删除；1->已删除
     */
    private Integer deleteStatus;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    private Date receiveTime;

    /**
     * 评价时间
     */
    private Date commentTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 备注
     */
    private String remark;

    private List<OrderItem> orderItemList;

    /**
     *  订单备注历史
     */
    private List<OrderRemarkLog> orderRemarkLogList;

    private BuyerInfoVo buyerInfoVo;

    private OrderAddress orderAddress;

    private String afterSalesStatus;

    private List<OrderOperationTime> orderOperationTimeList;

}
