package com.ruoyi.toc.converter;

import com.ruoyi.toc.entity.PurchaseOrderItem;
import com.ruoyi.toc.qo.PurchaseOrderItemQo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseOrderItemConverter {

    PurchaseOrderItemConverter INSTANCE = Mappers.getMapper(PurchaseOrderItemConverter.class);

    PurchaseOrderItem toPurchaseOrderItemEntity(PurchaseOrderItemQo purchaseOrderItemQo);


}
