package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ums_delivery_address")
public class DeliveryAddress implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消费者id
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 收货人姓名
     */
    @TableField(value = "receiver_name")
    private String receiverName;

    /**
     * 收货人手机号码
     */
    @TableField(value = "receiver_phone")
    private String receiverPhone;

    /**
     * 省份
     */
    @TableField(value = "province")
    private String province;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 区域
     */
    @TableField(value = "region")
    private String region;

    /**
     * 省份label
     */
    @TableField(value = "province_label")
    private String provinceLabel;

    /**
     * 城市label
     */
    @TableField(value = "city_label")
    private String cityLabel;

    /**
     * 区域label
     */
    @TableField(value = "region_label")
    private String regionLabel;

    /**
     * 地址详情
     */
    @TableField(value = "address")
    private String address;

    /**
     * 是否位默认地址：1-是，0-否
     */
    @TableField(value = "default_address")
    private Integer defaultAddress;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

}
