package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("oms_order_remark_log")
@Accessors(chain = true)
public class OrderRemarkLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单id */
    @TableField(value = "order_id")
    private Long orderId;

    /** 订单状态：waiting_pay->待付款；waiting_delivery->待发货；delivered->已发货；completed->已完成；closed->已关闭； */
    @TableField(value = "order_status")
    private String orderStatus;


    /** 创建者 */
    @TableField(value = "create_by")
    private String createBy;

    /** 创建时间 */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /** 备注 */
    @TableField(value = "remark")
    private String remark;

}
