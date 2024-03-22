package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.qo.ProductCollectionQo;

import java.util.List;

public interface ProductCollectionService {
    void saveMyProductCollection(ProductCollection productCollection);

    Page queryMyProductCollectionList(ProductCollectionQo productCollectionQo);

    void batchCancelCollect(List<Long> ids);

    List<ProductCategory> queryProductCategory();

}
