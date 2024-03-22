package com.ruoyi.toc.converter;

import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.vo.ProductCategoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductCategoryConverter {

    ProductCategoryConverter INSTANCE = Mappers.getMapper(ProductCategoryConverter.class);

    List<ProductCategoryVo> toProductCategoryVoList(List<ProductCategory> productCategoryList);

}
