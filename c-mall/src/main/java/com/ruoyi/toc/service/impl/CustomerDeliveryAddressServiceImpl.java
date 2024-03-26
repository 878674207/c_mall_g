package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.toc.entity.DeliveryAddress;
import com.ruoyi.toc.mapper.DeliveryAddressMapper;
import com.ruoyi.toc.service.CustomerDeliveryAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomerDeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddress> implements CustomerDeliveryAddressService {


    @Override
    public void saveDeliveryAddress(DeliveryAddress deliveryAddress) {
        if (Objects.isNull(deliveryAddress.getId())) {
            deliveryAddress.setCreateTime(DateUtils.getNowDate());
        }
        deliveryAddress.setUpdateTime(DateUtils.getNowDate());
        deliveryAddress.setCustomerId(SecurityUtils.getCustomerLoginUserId());
        // 保证一个用户只有一个默认地址
        handDefaultAddress(deliveryAddress);
        saveOrUpdate(deliveryAddress);
    }

    private void handDefaultAddress(DeliveryAddress deliveryAddress) {
        DeliveryAddress defaultAddress = queryDefaultAddress();
        if (Objects.isNull(defaultAddress)
                || deliveryAddress.getDefaultAddress() == 0
                || (Objects.nonNull(deliveryAddress.getId()) && deliveryAddress.getId().longValue() == defaultAddress.getId().longValue())) {
            return;
        }
        DeliveryAddress update = new DeliveryAddress();
        update.setId(defaultAddress.getId());
        update.setDefaultAddress(0);
        updateById(update);
    }


    @Override
    public DeliveryAddress queryMyDefaultAddress() {
        DeliveryAddress defaultAddress = getOne(new LambdaQueryWrapper<DeliveryAddress>()
                .eq(DeliveryAddress::getCustomerId, SecurityUtils.getCustomerLoginUserId())
                .eq(DeliveryAddress::getDefaultAddress, 1).last("limit 1"));
        if (Objects.nonNull(defaultAddress)) {
            return defaultAddress;
        }

        // 没有设置默认地址取收获地址第一条
        List<DeliveryAddress> list = list(new LambdaQueryWrapper<DeliveryAddress>()
                .eq(DeliveryAddress::getCustomerId, SecurityUtils.getCustomerLoginUserId())
                .orderByDesc(DeliveryAddress::getUpdateTime));
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private DeliveryAddress queryDefaultAddress() {
        return getOne(new LambdaQueryWrapper<DeliveryAddress>()
                .eq(DeliveryAddress::getCustomerId, SecurityUtils.getCustomerLoginUserId())
                .eq(DeliveryAddress::getDefaultAddress, 1).last("limit 1"));
    }


    @Override
    public void deleteDeliveryAddress(List<Long> ids) {
        removeBatchByIds(ids);
    }

    @Override
    public Page queryDeliveryAddressList(BaseQo baseQo) {
        return page(baseQo.initPage(), new LambdaQueryWrapper<DeliveryAddress>()
                .eq(DeliveryAddress::getCustomerId, SecurityUtils.getCustomerLoginUserId())
                .orderByDesc(DeliveryAddress::getDefaultAddress));
    }


}
