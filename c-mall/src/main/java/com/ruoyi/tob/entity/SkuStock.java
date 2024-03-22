package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("pms_sku_stock")
@Accessors(chain = true)
public class SkuStock implements Serializable {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** $column.columnComment */
    @ApiModelProperty(value = "商品id")
    @TableField(value = "product_id")
    private Long productId;

    @ApiModelProperty(value = "sku编码")
    @TableField(value = "sku_code")
    /** sku编码 */
    private String skuCode;

    @ApiModelProperty(value = "价格")
    @TableField(value = "price")
    /** $column.columnComment */
    @NotNull(message = "销售价格不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "库存")
    @TableField(value = "stock")
    /** 库存 */
    @NotNull(message = "商品库存不能为空")
    private Long stock;

    @ApiModelProperty(value = "预警库存")
    @TableField(value = "low_stock")
    /** 预警库存 */
    private Long lowStock;

    @ApiModelProperty(value = "展示图片")
    @TableField(value = "pic")
    /** 展示图片 */
    private String pic;

    @ApiModelProperty(value = "销量")
    @TableField(value = "sale")
    /** 销量 */
    private Long sale;

    @ApiModelProperty(value = "单品促销价格")
    @TableField(value = "promotion_price")
    /** 单品促销价格 */
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "锁定库存")
    @TableField(value = "lock_stock")
    /** 锁定库存 */
    private Long lockStock;

    @ApiModelProperty(value = "商品销售属性，json格式")
    @TableField(value = "sp_data")
    /** 商品销售属性，json格式 */
    private String spData;
}
