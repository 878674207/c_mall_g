package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("ums_purchase_order_item")
@Accessors(chain = true)
public class PurchaseOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 采购单id */
    private Long purchaseOrderId;

    /** 店铺id */
    private Long storeId;

    /** 店铺名称 */
    private String storeName;

    /** 商品id */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品主图 */
    private String productPic;

    /** $column.columnComment */
    private String productSn;

    /** 销售价格 */
    private BigDecimal productPrice;

    /** 购买数量 */
    private Long quantity;

    /** 商品sku编号 */
    private Long productSkuId;

    /** 商品sku条码 */
    private String productSkuCode;

    /** 商品分类id */
    private Long productCategoryId;

    /** 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}] */
    private String productAttr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;
}
