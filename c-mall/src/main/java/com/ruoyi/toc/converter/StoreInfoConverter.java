package com.ruoyi.toc.converter;

import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.toc.vo.StoreInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreInfoConverter {

    StoreInfoConverter INSTANCE = Mappers.getMapper(StoreInfoConverter.class);

    StoreInfoVo toStoreInfoVo(StoreInfo storeInfo);

}
