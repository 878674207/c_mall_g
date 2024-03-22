package com.ruoyi.toc.qo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PurchaseOrderItemQo {

    private Long id;

    /**
     *  操作类型  remove->移除  transfer-> 转移
     */
    private String operationType;

    /**
     * 采购单id
     */
    @NotNull(message = "采购单id不能为空")
    private Long purchaseOrderId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productPic;

    /**
     * $column.columnComment
     */
    private String productSn;

    /**
     * 销售价格
     */
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    private Long quantity;

    /**
     * 商品sku编号
     */
    private Long productSkuId;

    /**
     * 商品sku条码
     */
    private String productSkuCode;

    /**
     * 商品分类id
     */
    private Long productCategoryId;

    /**
     * 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}]
     */
    private String productAttr;

}
