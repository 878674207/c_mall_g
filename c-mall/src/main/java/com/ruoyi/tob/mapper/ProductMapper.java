package com.ruoyi.tob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    List<Product> queryMaxCollectedProductByStoreId(@Param("storeId") Long storeId);

    List<Long> queryProductCategoryIdByStoreId(@Param("storeId") Long storeId);

}
