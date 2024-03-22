package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("pms_product_category")
@Accessors(chain = true)
public class ProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上机分类的编号：0表示一级分类
     */
    private Long parentId;

    /**
     * $column.columnComment
     */
    private String categoryName;

    /**
     * 分类级别：(0 一级；1 二级)
     */
    private Integer level;


    /**
     * 显示状态：（0不显示；1显示）
     */
    private Integer showStatus;

    /**
     * $column.columnComment
     */
    private Long sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * $column.columnComment
     */
    private String keywords;

}
