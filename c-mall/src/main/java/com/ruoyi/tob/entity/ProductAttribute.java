package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("pms_product_attribute")
@Accessors(chain = true)
public class ProductAttribute implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品属性分类id
     */
    private Long productAttributeCategoryId;

    private String productAttributeCategoryName;

    /**
     * 属性（或参数）名称
     */
    private String productAttributeName;

    /**
     * 参数选择类型：single-单选；multiple-多选
     */
    private String selectType;

    /**
     * 参数录入方式：input-手工录入；select-从列表中选取
     */
    private String inputType;

    /**
     * 可选值列表，以逗号隔开
     */
    private String inputList;

    /**
     * 排序字段：最高的可以单独上传图片
     */
    private Long sort;

    /**
     * 是否支持手动新增；0->不支持；1->支持
     */
    private Integer handAddStatus;

    /**
     * 属性的类型：attribute-属性；params-参数
     */
    private String type;
}
