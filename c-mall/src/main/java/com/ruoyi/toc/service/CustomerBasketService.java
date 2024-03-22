package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.qo.BasketQo;
import com.ruoyi.toc.vo.ConfirmOrderVo;

import java.util.List;

public interface CustomerBasketService {
    Page myBasketList(BaseQo baseQo);

    void saveBasket(BasketQo basketQo) throws CommonException;

    void deleteBasket(BasketQo basketQo);

    void clearBasket();

    ConfirmOrderVo settleBasket(List<BasketQo> basketQoList);

    void refreshBasket();

    void transferCollection(BasketQo basketQo);
}
