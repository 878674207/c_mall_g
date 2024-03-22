package com.ruoyi.tob.service;

import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.qo.SkuStockQo;

import java.util.List;

public interface SkuStockService {
    /**
     * 根据产品id和skuCode关键字模糊搜索
     */
    List<SkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    void update( List<SkuStock> skuStockList) throws CommonException;

    void del(String[] ids);

    void lockSkuStock(List<SkuStockQo> skuStockQoList) throws CommonException;

    void unLockSkuStock(List<SkuStockQo> skuStockQoList) throws CommonException;

    void reduceSkuStock(List<SkuStockQo> skuStockQoList) throws CommonException;
}
