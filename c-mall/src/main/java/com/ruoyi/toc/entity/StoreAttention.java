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
@TableName("ums_store_attention")
@Accessors(chain = true)
public class StoreAttention implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 消费者id */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 商品id */
    @TableField(value = "store_id")
    private Long storeId;

    /** 店铺名称 */
    @TableField(value = "store_name")
    private String storeName;

    /** logo地址 */
    @TableField(value = "store_logo")
    private String storeLogo;

    /** 是否已关注：1-是，0-否 */
    @TableField(value = "attend")
    private Integer attend;

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
