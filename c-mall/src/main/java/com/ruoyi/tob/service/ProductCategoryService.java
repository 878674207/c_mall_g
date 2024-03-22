package com.ruoyi.tob.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.vo.ProductCategoryVo;

import java.util.List;

public interface ProductCategoryService {
    Page queryProductCategoryPageList(BaseQo baseQo);

    ProductCategory queryProductCategoryDetail(Long id) throws CommonException;

    void deleteProductCategory(Long id) throws CommonException;

    void saveProductCategory(ProductCategory productCategory);

    List<ProductCategory> queryProductCategoryList(Long parentId);

    List<ProductCategoryVo> queryProductCategoryWithChildren();

    Object querycategoryTree();
}
