package com.ruoyi.tob.converter;

import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.qo.ProductQo;
import com.ruoyi.tob.vo.ProductVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductConverter {

    ProductConverter INSTANCE = Mappers.getMapper(ProductConverter.class);

    Product toProductEntity(ProductQo productQo);

    ProductVo toProductVo(Product product);

    List<ProductVo> toProductVoList(List<Product> products);

}
