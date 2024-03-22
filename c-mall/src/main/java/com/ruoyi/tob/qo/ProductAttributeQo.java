package com.ruoyi.tob.qo;

import com.ruoyi.common.core.qo.BaseQo;
import lombok.Data;

@Data
public class ProductAttributeQo extends BaseQo {
    private Long productAttributeCategoryId;

    private String type;
}
