package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("pms_product_attribute_value")
public class ProductAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** $column.columnComment */
    private Long productId;

    /** $column.columnComment */
    private Long productAttributeId;

    private String productAttributeName;

    /** 手动添加规格或参数的值，参数单值，规格有多个时以逗号隔开 */
    private String value;
}
