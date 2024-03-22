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
import java.util.List;

@Data
@TableName("ums_purchase_order")
@Accessors(chain = true)
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 消费者id */
    private Long customerId;

    /** 采购单标题 */
    private String purchaseOrderName;

    /** 采购单描述 */
    private String purchaseOrderDescribe;

    /** 采购周期 */
    private String purchasingCycle;

    /** 可见性(仅自己可见:private; 公开:public;) */
    private String visible;

    /** 是否为默认采购单：1-是，0-否 */
    private Integer defaultPurchaseOrder;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private List<PurchaseOrderItem> purchaseOrderItems;

    private Integer productQuantity;
}
