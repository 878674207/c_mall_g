package com.ruoyi.toc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.DeliveryAddress;

import java.util.List;

public interface CustomerDeliveryAddressService {
    void saveDeliveryAddress(DeliveryAddress deliveryAddress);

    DeliveryAddress queryMyDefaultAddress();

    void deleteDeliveryAddress(List<Long> ids);

    Page queryDeliveryAddressList(BaseQo baseQo);
}
