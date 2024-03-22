package com.ruoyi.toc.service;

import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.vo.StoreInfoVo;

import java.util.List;

public interface StoreClientService {
    StoreInfoVo queryClientStoreDetail(Long id);

    Object queryStoreRecommend(Long storeId, String type);

    List<ProductCategoryVo> queryStoreProductCategory(Long storeId);
}
