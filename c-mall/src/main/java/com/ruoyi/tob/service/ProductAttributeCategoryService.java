package com.ruoyi.tob.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductAttributeCategory;

import java.util.List;

public interface ProductAttributeCategoryService {
    void saveProductAttributeCategory(ProductAttributeCategory productAttributeCategory);

    Page<ProductAttributeCategory> queryProductAttributeCategoryPageList(BaseQo baseQo);

    void deleteProductAttributeCategory(Long id) throws CommonException;

    List<ProductAttributeCategory> queryProductAttributeCategoryAllList();

}
