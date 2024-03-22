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
@TableName("ums_product_collection")
@Accessors(chain = true)
public class ProductCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 消费者id */
    @TableField(value = "customer_id")
    private Long customerId;

    /** 商品id */
    @TableField(value = "product_id")
    private Long productId;

    /** 商品名称 */
    @TableField(value = "product_name")
    private String productName;

    /** 副标题 */
    @TableField(value = "sub_title")
    private String subTitle;

    /** 商品封面图 */
    @TableField(value = "pic")
    private String pic;

    /** 是否已收藏：1-是，0-否 */
    @TableField(value = "collected")
    private Integer collected;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "store_id")
    private Long storeId;

    @TableField(value = "product_category_id")
    private Long productCategoryId;

}
