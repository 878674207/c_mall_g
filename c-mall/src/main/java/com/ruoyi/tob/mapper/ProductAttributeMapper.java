package com.ruoyi.tob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.entity.ProductAttribute;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductAttributeMapper extends BaseMapper<ProductAttribute> {
    int updateAttributeToNull(Long id);
}
