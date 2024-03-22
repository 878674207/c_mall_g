package com.ruoyi.toc.converter;

import com.ruoyi.toc.entity.Basket;
import com.ruoyi.toc.entity.BasketItem;
import com.ruoyi.toc.vo.BasketItemVo;
import com.ruoyi.toc.vo.BasketVo;
import com.ruoyi.toc.vo.ConfirmOrder;
import com.ruoyi.toc.vo.ConfirmOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BasketConverter {
    BasketConverter INSTANCE = Mappers.getMapper(BasketConverter.class);

    List<BasketItemVo> toBasketItemVoList(List<BasketItem> basketItemList);

    List<BasketVo> toBasketVoList(List<Basket> basketList);




    List<ConfirmOrderItem> toConfirmOrderItemList(List<BasketItem> basketItemList);

    List<ConfirmOrder> toConfirmOrderList(List<Basket> basketList);

}
