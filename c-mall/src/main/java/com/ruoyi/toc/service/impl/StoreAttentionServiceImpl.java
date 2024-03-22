package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.toc.entity.StoreAttention;
import com.ruoyi.toc.mapper.StoreAttentionMapper;
import com.ruoyi.toc.service.StoreAttentionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StoreAttentionServiceImpl extends ServiceImpl<StoreAttentionMapper,StoreAttention> implements StoreAttentionService {

    @Autowired
    private StoreAttentionMapper storeAttentionMapper;

    @Override
    public void saveMyStoreAttention(StoreAttention storeAttention) {
        storeAttention.setCustomerId(SecurityUtils.getCurWechatLoginUser().getId());
        storeAttention.setUpdateTime(DateUtils.getNowDate());
        storeAttention.setCreateTime(DateUtils.getNowDate());
        storeAttentionMapper.saveStoreAttention(storeAttention);
    }

    @Override
    public Page queryMyStoreAttentionList(BaseQo baseQo) {
        Page page = baseQo.initPage();

        storeAttentionMapper.selectPage(page, new LambdaQueryWrapper<StoreAttention>()
                .eq(StoreAttention::getCustomerId, SecurityUtils.getCurWechatLoginUser().getId())
                .eq(StoreAttention::getAttend, 1)
                .like(StringUtils.isNotEmpty(baseQo.getKeyword()), StoreAttention::getStoreName, baseQo.getKeyword())
                .orderByDesc(StoreAttention::getUpdateTime));

        return page;
    }

    @Override
    public void batchCancelAttention(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<StoreAttention> list = Lists.newArrayList();
        ids.forEach(item -> list.add(new StoreAttention().setId(item).setAttend(0)));
        updateBatchById(list);
    }
}
