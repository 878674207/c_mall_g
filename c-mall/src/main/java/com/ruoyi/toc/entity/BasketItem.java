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
@TableName("ums_basket_item")
@Accessors(chain = true)
public class BasketItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 购物车id
     */
    @TableField(value = "basket_id")
    private Long basketId;

    /**
     * 商品id
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * sku表id
     */
    @TableField(value = "product_sku_id")
    private Long productSkuId;

    /**
     * 购买数量
     */
    @TableField(value = "quantity")
    private Long quantity;


    /**
     * 第一次加入购物车单价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 添加到购物车的价格
     */
    @TableField(value = "price")
    private BigDecimal price;


    /**
     * 商品行总价
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 商品图片(sku图片)
     */
    @TableField(value = "product_pic")
    private String productPic;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 商品副标题（卖点）
     */
    @TableField(value = "product_sub_title")
    private String productSubTitle;

    /**
     * 商品sku条码
     */
    @TableField(value = "product_sku_code")
    private String productSkuCode;

    /**
     * 是否删除
     */
    @TableField(value = "delete_status")
    private Integer deleteStatus;

    /**
     * 商品分类
     */
    @TableField(value = "product_category_id")
    private Long productCategoryId;

    /**
     * $column.columnComment
     */
    @TableField(value = "product_sn")
    private String productSn;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    @TableField(value = "product_attr")
    private String productAttr;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     *  是否已过期  1-过期；0-未过期
     */
    @TableField(value = "expired")
    private Integer expired;
}
