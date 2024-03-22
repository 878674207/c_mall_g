package com.ruoyi.toc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ConfirmOrderItem {


    private Long id;

    /**
     * 购物车id
     */
    private Long basketId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * sku表id
     */
    private Long productSkuId;

    /**
     * 购买数量
     */
    private Long quantity;

    /**
     * 添加到购物车的价格
     */
    private BigDecimal price;


    /**
     * 商品行总价
     */
    private BigDecimal totalPrice;

    /**
     * 商品主图
     */
    private String productPic;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品副标题（卖点）
     */
    private String productSubTitle;

    /**
     * 商品sku条码
     */
    private String productSkuCode;

    /**
     * 是否删除
     */
    private Integer deleteStatus;

    /**
     * 商品分类
     */
    private Long productCategoryId;

    /**
     * $column.columnComment
     */
    private String productSn;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttr;

}
