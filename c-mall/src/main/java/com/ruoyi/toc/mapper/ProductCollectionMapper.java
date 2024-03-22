package com.ruoyi.toc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.toc.entity.ProductCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCollectionMapper extends BaseMapper<ProductCollection> {
    void batchSaveProductCollection(@Param("productCollectionList") List<ProductCollection> productCollectionList);
}
