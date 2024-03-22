package com.ruoyi.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.CusImage;
import com.ruoyi.system.mapper.CusImageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CusImageService extends ServiceImpl<CusImageMapper, CusImage>  {

    public void batchUpdate(List<CusImage> list, String businessType, Long businessId) {
        // 逻辑删除
        CusImage update = new CusImage();
        update.setDelFlag(1);
        update(update,new QueryWrapper<CusImage>().eq("business_type", businessType).eq("business_id", businessId));
        list.forEach( item -> {
            item.setId(null);
            item.setBusinessType(businessType);
            item.setDelFlag(0);
            item.setBusinessId(Long.valueOf(businessId));
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
        });
        saveBatch(list);
    }


    public void batchSaveByType(List<CusImage> list, String businessType, Long businessId) {
        list.forEach(item -> {
            item.setBusinessType(businessType);
            item.setDelFlag(0);
            item.setBusinessId(businessId);
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
        });
        saveBatch(list);
    }

    public List<CusImage> queryCusImageList(String businessType, List<Long> businessIds) {
        return list(new QueryWrapper<CusImage>().eq("business_type", businessType).in("business_id", businessIds));
    }

}
