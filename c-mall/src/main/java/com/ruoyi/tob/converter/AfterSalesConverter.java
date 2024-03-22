package com.ruoyi.tob.converter;

import com.ruoyi.tob.vo.AfterSalesVo;
import com.ruoyi.toc.entity.AfterSales;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AfterSalesConverter {

    AfterSalesConverter INSTANCE = Mappers.getMapper(AfterSalesConverter.class);


    AfterSalesVo toAfterSalesVo(AfterSales afterSales);
}
