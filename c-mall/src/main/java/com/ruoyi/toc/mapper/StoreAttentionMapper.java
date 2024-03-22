package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.toc.entity.StoreAttention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StoreAttentionMapper extends BaseMapper<StoreAttention> {
    void saveStoreAttention(@Param("storeAttention") StoreAttention storeAttention);
}
