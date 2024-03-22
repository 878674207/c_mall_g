package com.ruoyi.tob.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.StoreInfo;

/**
 * @author Lis
 * @Description:
 * @date 2023/12/27
 */

public interface StoreInfoService extends IService<StoreInfo> {
    Page<StoreInfo> show(StoreInfo storeInfo, BaseQo page);

    StoreInfo saveStore(StoreInfo storeInfo);

    void updateStore(StoreInfo storeInfo);

    StoreInfo currentStoreInfo();

    StoreInfo selectById(String id);

    void forbid(String id,String storeStatus, String forbidReason);
}
