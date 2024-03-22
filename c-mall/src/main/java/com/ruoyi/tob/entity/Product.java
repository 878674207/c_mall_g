package com.ruoyi.tob.entity;

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
@TableName("pms_product")
@Accessors(chain = true)
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "product_category_id")
    private Long productCategoryId;

    /**
     * 商品分类名称
     */
    @TableField(value = "product_category_name")
    private String productCategoryName;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 副标题
     */
    @TableField(value = "sub_title")
    private String subTitle;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 货号
     */
    @TableField(value = "product_sn")
    private String productSn;

    /**
     * 单位
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 商品售价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 市场价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Long stock;

    /**
     * 商品重量，默认为克
     */
    @TableField(value = "weight")
    private BigDecimal weight;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Long sort;

    /**
     * 商品封面图
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 商品属性分类id
     */
    @TableField(value = "product_attribute_category_id")
    private Long productAttributeCategoryId;

    /**
     * 商品详情富文本
     */
    @TableField(value = "detail_html")
    private String detailHtml;


    /**
     * 上架状态：0->下架；1->上架
     */
    @TableField(value = "publish_status")
    private Integer publishStatus;

    /**
     * 新品状态:0->不是新品；1->新品
     */
    @TableField(value = "new_status")
    private Integer newStatus;

    /**
     * 推荐状态；0->不推荐；1->推荐
     */
    @TableField(value = "recommand_status")
    private Integer recommandStatus;

    /**
     * 审核状态  pending_approval-待审核;approved-审核通过;rejected-审核不通过
     */
    @TableField(value = "approve_status")
    private String approveStatus;



    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "sale")
    private Long sale;

    @TableField(value = "visit_count")
    private Long visitCount;

    @TableField(value = "store_id")
    private Long storeId;

    @TableField(value = "store_name")
    private String storeName;

    @TableField(value = "approval_comment")
    private String approvalComment;
}
