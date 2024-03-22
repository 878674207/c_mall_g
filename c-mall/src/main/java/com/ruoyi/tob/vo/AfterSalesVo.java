package com.ruoyi.tob.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.tob.entity.AfterSalesRemarkLog;
import com.ruoyi.toc.entity.AfterSalesItem;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class AfterSalesVo {

    private Long id;

    /** 消费者id */
    private Long customerId;

    /** 店铺id */
    private Long storeId;

    /** 店铺名称 */
    private String storeName;


    /** 售后编号*/
    private String afterSalesNo;


    /** 订单编号 */
    private String orderNo;

    /** 售后原因 */
    private String afterSalesReason;

    /** 售后备注 */
    private String afterSalesRemark;

    /** 售后状态 */
    private String afterSalesStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    private List<AfterSalesItem> afterSalesItemList;

    private BuyerInfoVo buyerInfoVo;

    private OrderAddress orderAddress;

    private Order orderInfo;


    private List<AfterSalesRemarkLog> afterSalesRemarkLogList;
}
