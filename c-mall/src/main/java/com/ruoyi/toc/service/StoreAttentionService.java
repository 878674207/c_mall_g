package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.StoreAttention;

import java.util.List;

public interface StoreAttentionService {
    void saveMyStoreAttention(StoreAttention storeAttention);

    Page queryMyStoreAttentionList(BaseQo baseQo);

    void batchCancelAttention(List<Long> ids);
}
