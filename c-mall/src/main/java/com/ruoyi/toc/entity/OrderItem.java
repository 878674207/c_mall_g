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
@TableName("oms_order_item")
@Accessors(chain = true)
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单id */
    @TableField(value = "order_id")
    private Long orderId;

    /** 商品id */
    @TableField(value = "product_id")
    private Long productId;

    /** 商品名称 */
    @TableField(value = "product_name")
    private String productName;

    /** 商品主图 */
    @TableField(value = "product_pic")
    private String productPic;

    /** $column.columnComment */
    @TableField(value = "product_sn")
    private String productSn;

    /** 销售价格 */
    @TableField(value = "product_price")
    private BigDecimal productPrice;

    /** 购买数量 */
    @TableField(value = "quantity")
    private Long quantity;

    /** 商品sku编号 */
    @TableField(value = "product_sku_id")
    private Long productSkuId;

    /** 商品sku条码 */
    @TableField(value = "product_sku_code")
    private String productSkuCode;

    /** 商品分类id */
    @TableField(value = "product_category_id")
    private Long productCategoryId;

    /** 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}] */
    @TableField(value = "product_attr")
    private String productAttr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

}
