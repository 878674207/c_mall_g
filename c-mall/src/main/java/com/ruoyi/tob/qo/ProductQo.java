package com.ruoyi.tob.qo;

import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.system.domain.CusImage;
import com.ruoyi.tob.entity.ProductAttributeValue;
import com.ruoyi.tob.entity.SkuStock;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "ProductQo", description = "商品信息入参")
public class ProductQo extends BaseQo {
    private Long id;

    private List<Long> idlist;

    /**
     *  操作类型  1-批量删除  2-批量下架  3-批量上架
     */
    private String operateType;

    @ApiModelProperty(value = "商品分类id")
    private Long productCategoryId;

    /**
     * 商品分类名称
     */
    @ApiModelProperty(value = "商品分类名称")
    private String productCategoryName;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subTitle;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String description;

    /**
     * 货号
     */
    @ApiModelProperty(value = "货号")
    private String productSn;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    private BigDecimal price;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Long stock;

    /**
     * 商品重量，默认为克
     */
    @ApiModelProperty(value = "商品重量，默认为克")
    private BigDecimal weight;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Long sort;

    /**
     * 商品封面图
     */
    @ApiModelProperty(value = "商品封面图")
    private String pic;

    @ApiModelProperty(value = "商品轮播图")
    private List<CusImage> carouselImages;

    /**
     * 商品属性分类id
     */
    @ApiModelProperty(value = "商品属性分类id")
    private Long productAttributeCategoryId;

    /**
     * 商品详情富文本
     */
    @ApiModelProperty(value = "商品详情富文本")
    private String detailHtml;


    /**
     * 上架状态：0->下架；1->上架
     */
    private Integer publishStatus;

    /**
     * 新品状态:0->不是新品；1->新品
     */
    private Integer newStatus;

    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    private Integer recommandStatus;

    /**
     * 审核状态  pending_approval-待审核;approved-审核通过;rejected-审核不通过
     */
    private String approveStatus;

    @ApiModelProperty(value = "商品库存")
    @Size(min = 1, message = "商品规格信息不能为空")
    @Valid
    private List<SkuStock> skuStockList;

    @ApiModelProperty(value = "商品参数值")
    private List<ProductAttributeValue> productAttributeValueList;
}
