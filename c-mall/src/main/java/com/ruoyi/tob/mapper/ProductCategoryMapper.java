package com.ruoyi.tob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.vo.ProductCategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    List<ProductCategoryVo> queryProductCategoryWithChildren();


    List<ProductCategoryVo> queryProductCategoryByChildrenIds(@Param("ids") List<Long> ids);
}
