package com.ruoyi.toc.entity;

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
@TableName("oms_order_address")
@Accessors(chain = true)
public class OrderAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单id */
    @TableField(value = "order_id")
    private Long orderId;

    /** 收货人姓名 */
    @TableField(value = "receiver_name")
    private String receiverName;

    /** 收货人手机号码 */
    @TableField(value = "receiver_phone")
    private String receiverPhone;

    /** 省份 */
    @TableField(value = "province")
    private String province;

    /** 城市 */
    @TableField(value = "city")
    private String city;

    /** 区域 */
    @TableField(value = "region")
    private String region;

    /** 地址详情 */
    @TableField(value = "address")
    private String address;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String orderNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;
}
