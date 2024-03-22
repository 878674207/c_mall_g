package com.ruoyi.tob.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.entity.ProductAttribute;
import com.ruoyi.tob.qo.ProductAttributeQo;

import java.util.List;

public interface ProductAttributeService {
    Page queryProductAttributePageList(ProductAttributeQo productAttributeQo);

    void saveProductAttribute(ProductAttribute productAttribute) throws CommonException;

    void deleteProductAttribute(Long id) throws CommonException;

    List<ProductAttribute> queryProductAttributeAllList(Long attributeCategoryId);
}
