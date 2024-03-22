package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("pms_product_attribute_category")
@Accessors(chain = true)
public class ProductAttributeCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** $column.columnComment */
    private String attributeCategoryName;

    /** 属性数量 */
    private Long attributeCount;

    /** 参数数量 */
    private Long paramCount;

    private Long storeId;

}
