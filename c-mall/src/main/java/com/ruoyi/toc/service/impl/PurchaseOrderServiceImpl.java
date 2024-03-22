package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.toc.converter.PurchaseOrderItemConverter;
import com.ruoyi.toc.entity.PurchaseOrder;
import com.ruoyi.toc.entity.PurchaseOrderItem;
import com.ruoyi.toc.mapper.PurchaseOrderItemMapper;
import com.ruoyi.toc.mapper.PurchaseOrderMapper;
import com.ruoyi.toc.qo.PurchaseOrderItemQo;
import com.ruoyi.toc.qo.PurchaseOrderQo;
import com.ruoyi.toc.service.PurchaseOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder>  implements PurchaseOrderService {


    private final String REMOVE = "remove";

    private final String TRANSFER = "transfer";



    @Autowired
    private PurchaseOrderItemMapper purchaseOrderItemMapper;


    @Override
    public void savePurchaseOrder(PurchaseOrder purchaseOrder) {
        if (Objects.isNull(purchaseOrder.getId())) {
            purchaseOrder.setCreateTime(DateUtils.getNowDate());
        }
        purchaseOrder.setCustomerId(SecurityUtils.getCurWechatLoginUserId());

        // 保证一个用户只有一个默认采购单
        handDefaultPurchaseOrder(purchaseOrder);

        saveOrUpdate(purchaseOrder);
    }

    private void handDefaultPurchaseOrder(PurchaseOrder purchaseOrder) {
        PurchaseOrder defaultPurchaseOrder = getOne(new LambdaQueryWrapper<PurchaseOrder>()
                .eq(PurchaseOrder::getCustomerId, SecurityUtils.getCurWechatLoginUserId())
                .eq(PurchaseOrder::getDefaultPurchaseOrder, 1).last("limit 1"));
        if (Objects.isNull(defaultPurchaseOrder)
                || purchaseOrder.getDefaultPurchaseOrder() == 0
                || (Objects.nonNull(purchaseOrder.getId()) && purchaseOrder.getId().longValue() == defaultPurchaseOrder.getId().longValue())) {
            return;
        }
        PurchaseOrder update = new PurchaseOrder();
        update.setId(defaultPurchaseOrder.getId());
        update.setDefaultPurchaseOrder(0);
        updateById(update);
    }

    @Override
    public void deletePurchaseOrder(Long id) throws CommonException {
        PurchaseOrder purchaseOrder = getById(id);
        if (Objects.isNull(purchaseOrder)) {
            throw new CommonException("该采购单不存在!");
        }
        removeById(id);
        purchaseOrderItemMapper.delete(new LambdaQueryWrapper<PurchaseOrderItem>().in(PurchaseOrderItem::getPurchaseOrderId, id));
    }

    @Override
    public Page<PurchaseOrder> queryPurchaseOrderList(PurchaseOrderQo purchaseOrderQo) {
        Page<PurchaseOrder> page = purchaseOrderQo.initPage();
        if (StringUtils.isNotBlank(purchaseOrderQo.getProductName())) {
            List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemMapper.selectList(new LambdaQueryWrapper<PurchaseOrderItem>()
                    .eq(PurchaseOrderItem::getProductName, purchaseOrderQo.getProductName()));
            if (CollectionUtils.isEmpty(purchaseOrderItems)) {
                return page;
            }
            purchaseOrderQo.setPurchaseOrderIds(purchaseOrderItems.stream().map(PurchaseOrderItem::getPurchaseOrderId).collect(Collectors.toSet()));
        }
        page(page, new LambdaQueryWrapper<PurchaseOrder>()
                .eq(PurchaseOrder::getCustomerId, SecurityUtils.getCurWechatLoginUserId())
                .in(CollectionUtils.isNotEmpty(purchaseOrderQo.getPurchaseOrderIds()), PurchaseOrder::getId, purchaseOrderQo.getPurchaseOrderIds())
                .orderByDesc(PurchaseOrder::getCreateTime));
        List<PurchaseOrder> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemMapper.selectList(new LambdaQueryWrapper<PurchaseOrderItem>()
                    .in(PurchaseOrderItem::getPurchaseOrderId, records.stream().map(PurchaseOrder::getId).collect(Collectors.toList())));
            records.forEach(item -> item.setPurchaseOrderItems(purchaseOrderItems.stream()
                    .filter(subItme -> subItme.getPurchaseOrderId().compareTo(item.getId()) == 0).collect(Collectors.toList())));
        }
        return page;
    }

    @Override
    public List<PurchaseOrder> queryAllPurchaseOrderList() {
        return list(new LambdaQueryWrapper<PurchaseOrder>().eq(PurchaseOrder::getCustomerId, SecurityUtils.getCurWechatLoginUserId())
                .last("order by default_purchase_order desc, create_time desc"));
    }

    @Override
    public PurchaseOrder queryHomePagePurchaseOrder() {
        List<PurchaseOrder> purchaseOrders = queryAllPurchaseOrderList();
        // 今天
        LocalDate today = LocalDate.now();
        for (PurchaseOrder item: purchaseOrders) {
            if ("week".equals(item.getPurchasingCycle())) {
                // 本周一
                LocalDate week1 = today.with(DayOfWeek.MONDAY);
                if (today.toString().equals(week1.toString())) {
                    return item;
                }
            }
            if ("month".equals(item.getPurchasingCycle())) {
                // 本月第一天
                LocalDate month1 = today.withDayOfMonth(1);
                if (today.toString().equals(month1.toString())) {
                    return item;
                }
            }
        }
        List<PurchaseOrder> defaultPurchaseOrder = purchaseOrders.stream().filter(item -> item.getDefaultPurchaseOrder().compareTo(1) == 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(defaultPurchaseOrder)) {
            return defaultPurchaseOrder.get(0);
        } else {
            return purchaseOrders.get(0);
        }
    }

    @Override
    public void addProduct(PurchaseOrderItemQo purchaseOrderItemQo) throws CommonException {
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItemMapper.selectOne(new LambdaQueryWrapper<PurchaseOrderItem>()
                .eq(PurchaseOrderItem::getPurchaseOrderId, purchaseOrderItemQo.getPurchaseOrderId())
                .eq(PurchaseOrderItem::getProductSkuId, purchaseOrderItemQo.getProductSkuId())
                .last("limit 1"));
        if (Objects.nonNull(purchaseOrderItem)) {
            return;
        }
        // 采购单表商品数量加一
        PurchaseOrder purchaseOrder = getById(purchaseOrderItemQo.getPurchaseOrderId());
        if (Objects.isNull(purchaseOrder)) {
            throw new CommonException("该采购单不存在!");
        }
        updateById(new PurchaseOrder().setId(purchaseOrder.getId()).setProductQuantity(purchaseOrder.getProductQuantity() + 1));

        // 加入采购单商品明细表
        PurchaseOrderItem purchaseOrderItemEntity = PurchaseOrderItemConverter.INSTANCE.toPurchaseOrderItemEntity(purchaseOrderItemQo);
        purchaseOrderItemEntity.setCreateTime(DateUtils.getNowDate());
        purchaseOrderItemMapper.insert(purchaseOrderItemEntity);
    }

    @Override
    public void updateProduct(PurchaseOrderItemQo purchaseOrderItemQo) {
        if (REMOVE.equals(purchaseOrderItemQo.getOperationType())) {
            purchaseOrderItemMapper.deleteById(purchaseOrderItemQo.getId());
            return;
        }

        if (TRANSFER.equals(purchaseOrderItemQo.getOperationType())) {
            purchaseOrderItemMapper.updateById(new PurchaseOrderItem()
                    .setId(purchaseOrderItemQo.getId()).setPurchaseOrderId(purchaseOrderItemQo.getPurchaseOrderId()));
        }
    }
}
