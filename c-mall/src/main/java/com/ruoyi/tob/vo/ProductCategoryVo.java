package com.ruoyi.tob.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVo {
    private Long id;


    private Long parentId;

    private String categoryName;

    private Long sort;

    private Integer level;

    /**
     * 图标
     */
    private String icon;

    private List<ProductCategoryVo> children;
}
