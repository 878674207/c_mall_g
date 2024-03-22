package com.ruoyi.tob.vo;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.system.domain.CusImage;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.entity.ProductAttributeValue;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.entity.StoreAttention;
import com.ruoyi.toc.vo.DeliveryVo;
import com.ruoyi.toc.vo.StoreInfoVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductVo {
    private Long id;

    private Long productCategoryId;

    /**
     * 商品分类名称
     */
    private String productCategoryName;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 货号
     */
    private String productSn;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品售价
     */
    private BigDecimal price;

    /**
     * 市场价
     */
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 商品重量，默认为克
     */
    private BigDecimal weight;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 商品封面图
     */
    private String pic;


    private List<CusImage> carouselImages;

    /**
     * 商品属性分类id
     */
    private Long productAttributeCategoryId;

    /**
     * 商品详情富文本
     */
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

    private List<SkuStock> skuStockList;

    private List<ProductAttributeValue> productAttributeValueList;


    /**
     *  是否露出审批按钮标识位
     */
    private Boolean approveFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long sale;

    private Long visitCount;

    private Long storeId;

    private String storeName;

    private List<Product> storeRecommend;

    private StoreInfoVo storeInfoVo;

    private ProductCollection productCollection;

    private StoreAttention storeAttention;

    private List<JSONObject> productAttribute;

    private DeliveryVo deliveryVo;
}
