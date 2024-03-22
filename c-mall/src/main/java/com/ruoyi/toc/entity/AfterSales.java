package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("oms_after_sales")
@Accessors(chain = true)
public class AfterSales implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 消费者id */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 店铺id */
    @TableField(value = "store_id")
    private Long storeId;

    /** 店铺名称 */
    @TableField(value = "store_name")
    private String storeName;


    /** 售后编号*/
    @TableField(value = "after_sales_no")
    private String afterSalesNo;


    /** 订单编号 */
    @TableField(value = "order_no")
    private String orderNo;

    /** 售后原因 */
    @TableField(value = "after_sales_reason")
    private String afterSalesReason;

    /** 售后备注 */
    @TableField(value = "after_sales_remark")
    private String afterSalesRemark;

    /** 售后状态 */
    @TableField(value = "after_sales_status")
    private String afterSalesStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    @Size(min = 1, message = "商品规格信息不能为空")
    @Valid
    private List<AfterSalesItem> afterSalesItemList;

}
