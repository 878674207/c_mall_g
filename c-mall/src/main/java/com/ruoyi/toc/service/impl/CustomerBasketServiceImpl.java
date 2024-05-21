package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.tob.service.impl.SkuStockServiceImpl;
import com.ruoyi.toc.converter.BasketConverter;
import com.ruoyi.toc.entity.Basket;
import com.ruoyi.toc.entity.BasketItem;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.mapper.BasketMapper;
import com.ruoyi.toc.mapper.ProductCollectionMapper;
import com.ruoyi.toc.qo.BasketQo;
import com.ruoyi.toc.service.CustomerBasketService;
import com.ruoyi.toc.service.CustomerDeliveryAddressService;
import com.ruoyi.toc.vo.BasketItemVo;
import com.ruoyi.toc.vo.BasketVo;
import com.ruoyi.toc.vo.ConfirmOrder;
import com.ruoyi.toc.vo.ConfirmOrderItem;
import com.ruoyi.toc.vo.ConfirmOrderVo;
import com.ruoyi.toc.vo.PaymentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CustomerBasketServiceImpl implements CustomerBasketService {

    @Autowired
    private BasketMapper basketMapper;

    @Autowired
    private BasketItemService basketItemService;


    @Autowired
    private SkuStockServiceImpl skuStockService;

    @Autowired
    private CustomerDeliveryAddressService customerDeliveryAddressService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCollectionMapper productCollectionMapper;

    @Override
    public Page myBasketList(BaseQo baseQo) {
        Page page = basketMapper.selectPage(baseQo.initPage(), new LambdaQueryWrapper<Basket>()
                .eq(Basket::getCustomerId, SecurityUtils.getCustomerLoginUserId()));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }

        // 遍历购物车，校验每个购物车明细单价是否发生变化
        List<Basket> records = page.getRecords();
        List<Long> basketIds = records.stream().map(Basket::getId).collect(Collectors.toList());
        List<BasketItem> basketItems = basketItemService.list(new LambdaQueryWrapper<BasketItem>().in(BasketItem::getBasketId, basketIds));
        if (CollectionUtils.isEmpty(basketItems)) {
            return page;
        }

        List<BasketItemVo> basketItemVoList = BasketConverter.INSTANCE.toBasketItemVoList(basketItems);
        List<BasketVo> basketVoList = BasketConverter.INSTANCE.toBasketVoList(records);

        List<Long> productSkuIds = basketItems.stream().map(BasketItem::getProductSkuId).collect(Collectors.toList());
        List<SkuStock> stockList = skuStockService.list(new LambdaQueryWrapper<SkuStock>().in(SkuStock::getId, productSkuIds));
        Map<String, BigDecimal> priceMap = stockList.stream().collect(Collectors.toMap(SkuStock::getSkuCode, SkuStock::getPrice));

        List<BasketItem> updateList = new ArrayList();
        basketItemVoList.stream().filter(item -> item.getExpired() == 0 ).forEach(item -> {
            BigDecimal productPrice = priceMap.get(item.getProductSkuCode());
            if (Objects.nonNull(productPrice) && item.getPrice().compareTo(priceMap.get(item.getProductSkuCode())) != 0) {
                BigDecimal curPrice = priceMap.get(item.getProductSkuCode());
                BigDecimal curTotalPrice = curPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setPrice(curPrice);
                item.setChangePrice(item.getPrice().subtract(item.getOriginalPrice()));
                item.setTotalPrice(curTotalPrice);
                updateList.add(new BasketItem().setId(item.getId()).setPrice(curPrice).setTotalPrice(curTotalPrice));
            } else if (Objects.isNull(productPrice)) {
                updateList.add(new BasketItem().setId(item.getId()).setExpired(1));
            }
        });

        // 批量更新购物车
        if (CollectionUtils.isNotEmpty(updateList)) {
            basketItemService.updateBatchById(updateList);
        }

        basketVoList.forEach(item -> item.setBasketItemList(
                basketItemVoList.stream().filter(basketItemVo -> item.getId().compareTo(basketItemVo.getBasketId()) == 0).collect(Collectors.toList())));
        page.setRecords(basketVoList);
        return page;
    }

    @Override
    public void saveBasket(BasketQo basketQo) {

        Basket basket = basketMapper.selectOne(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getStoreId, basketQo.getStoreId())
                .eq(Basket::getCustomerId, SecurityUtils.getCustomerLoginUserId())
                .last("limit 1"));

        // 该店铺没有购物车
        if (Objects.isNull(basket)) {
            // 插入购物车
            Basket newBasket = addBasket(basketQo);
            // 插入购物车明细
            addBasketItem(basketQo.setBasketId(newBasket.getId()));
            return;
        }

        // 该店铺存在购物车
        BasketItem basketItem = basketItemService.getOne(new LambdaQueryWrapper<BasketItem>()
                .eq(BasketItem::getBasketId, basket.getId())
                .eq(BasketItem::getProductId, basketQo.getProductId())
                .eq(BasketItem::getProductSkuId, basketQo.getProductSkuId())
                .last("limit 1"));

        // 不存在该商品 插入购物车明细
        if (Objects.isNull(basketItem)) {
            addBasketItem(basketQo.setBasketId(basket.getId()));
            return;
        }


        // 已经存在该商品，合并数量和总价
        updateBasketItem(basketItem, basketQo);

    }

    @Override
    public void deleteBasket(BasketQo basketQo) {
        List<Long> basketItemIds = basketQo.getBasketItemIds();
        if (CollectionUtils.isEmpty(basketItemIds)) {
            return;
        }
        basketItemService.removeBatchByIds(basketItemIds);
        refreshBasket();
    }

    /**
     *  清空商品明细为空的购物车
     */
    public void refreshBasket() {
        List<BasketVo> basketVos = basketMapper.queryBasketList(new BasketQo().setCustomerId(SecurityUtils.getCustomerLoginUserId()));
        if (CollectionUtils.isEmpty(basketVos)) {
            return;
        }
        List<Long> basketIds = Lists.newArrayList();
        basketVos.forEach(item -> {
            if (CollectionUtils.isEmpty(item.getBasketItemList())) {
                basketIds.add(item.getId());
            }
        });
        if (CollectionUtils.isEmpty(basketIds)) {
            return;
        }
        basketMapper.deleteBatchIds(basketIds);
    }

    @Override
    public void transferCollection(BasketQo basketQo) {
        // 查询购物车商品明细
        if (CollectionUtils.isEmpty(basketQo.getBasketItemIds())) {
            return;
        }
        List<BasketItem> basketItemList = basketItemService.list(new LambdaQueryWrapper<BasketItem>()
                .in(BasketItem::getId, basketQo.getBasketItemIds()));
        if (CollectionUtils.isEmpty(basketItemList)) {
            return;
        }
        Set<Long> productIds = basketItemList.stream().map(BasketItem::getProductId).collect(Collectors.toSet());

        // 查询商品详情
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>().in(Product::getId, productIds));
        if (CollectionUtils.isEmpty(productList)) {
            return;
        }

        // 加入收藏
        List<ProductCollection> list = Lists.newArrayList();
        productList.forEach(item -> {
            list.add(new ProductCollection()
                    .setCustomerId(SecurityUtils.getCustomerLoginUserId())
                    .setProductId(item.getId())
                    .setProductName(item.getProductName())
                    .setSubTitle(item.getSubTitle())
                    .setPic(item.getPic())
                    .setCollected(1)
                    .setPrice(item.getPrice())
                    .setStoreId(item.getStoreId())
                    .setProductCategoryId(item.getProductCategoryId())
                    .setCreateTime(DateUtils.getNowDate())
                    .setUpdateTime(DateUtils.getNowDate()));
        });
        productCollectionMapper.batchSaveProductCollection(list);

        // 删除购物车
        deleteBasket(basketQo);
    }

    @Override
    public void clearBasket() {
        List<Basket> baskets = basketMapper.selectList(new LambdaQueryWrapper<Basket>()
                .eq(Basket::getCustomerId, SecurityUtils.getCustomerLoginUserId()));
        if (CollectionUtils.isEmpty(baskets)) {
            return;
        }
        List<Long> basketIds = baskets.stream().map(Basket::getId).collect(Collectors.toList());
        basketItemService.remove(new LambdaQueryWrapper<BasketItem>().in(BasketItem::getBasketId, basketIds));
        basketMapper.deleteBatchIds(basketIds);
    }

    @Override
    public ConfirmOrderVo settleBasket(List<BasketQo> basketQoList) {
        ConfirmOrderVo confirmOrderVo = new ConfirmOrderVo();
        confirmOrderVo.setOrderAddress(customerDeliveryAddressService.queryMyDefaultAddress());

        // 获取结算购物车数据
        List<Basket> baskets = basketMapper.selectList(new LambdaQueryWrapper<Basket>().in(Basket::getId,
                basketQoList.stream().map(BasketQo::getBasketId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(baskets)) {
            return confirmOrderVo;
        }
        List<ConfirmOrder> confirmOrderList = BasketConverter.INSTANCE.toConfirmOrderList(baskets);

        // 获取结算商品数据
        List<Long> basketItemIds = basketQoList.stream().map(BasketQo::getBasketItemIds).flatMap(Collection::stream).collect(Collectors.toList());
        List<BasketItem> basketItems = basketItemService.list(new LambdaQueryWrapper<BasketItem>().in(BasketItem::getId, basketItemIds));
        if (CollectionUtils.isEmpty(basketItems)) {
            return confirmOrderVo;
        }
        List<ConfirmOrderItem> confirmOrderItems = BasketConverter.INSTANCE.toConfirmOrderItemList(basketItems);

        // 购物车商品组装
        confirmOrderList.forEach(item -> item.setConfirmOrderItems(
                confirmOrderItems.stream().filter(confirmOrderItem -> item.getId().compareTo(confirmOrderItem.getBasketId()) == 0).collect(Collectors.toList())));

        confirmOrderVo.setConfirmOrderList(confirmOrderList);


        BigDecimal productTotalPrice = BigDecimal.ZERO;
        for (ConfirmOrderItem item: confirmOrderItems) {
            productTotalPrice = productTotalPrice.add(item.getTotalPrice());
        }
        // 支付信息 其中运费暂时写死为0
        PaymentVo paymentVo = new PaymentVo();
        paymentVo.setDeliverPrice(BigDecimal.ZERO);
        paymentVo.setProductTotalPrice(productTotalPrice);
        paymentVo.setTotalPrice(paymentVo.getProductTotalPrice().add(paymentVo.getDeliverPrice()));
        confirmOrderVo.setPaymentVo(paymentVo);

        return confirmOrderVo;
    }

    private Basket addBasket(BasketQo basketQo) {
        Basket basket = new Basket()
                .setStoreId(basketQo.getStoreId()).setStoreName(basketQo.getStoreName())
                .setCustomerId(SecurityUtils.getCustomerLoginUserId())
                .setCreateTime(DateUtils.getNowDate())
                .setUpdateTime(DateUtils.getNowDate());
        basketMapper.insert(basket);
        return basket;
    }

    private void addBasketItem(BasketQo basketQo) {
        Product product = productMapper.selectById(basketQo.getProductId());
        SkuStock skuStock = skuStockService.getById(basketQo.getProductSkuId());
        if (Objects.isNull(product) || Objects.isNull(skuStock)) {
            throw new CommonException("商品不存在");
        }

        BasketItem basketItem = new BasketItem()
                .setBasketId(basketQo.getBasketId())
                .setProductId(product.getId())
                .setProductSkuId(skuStock.getId())
                .setQuantity(basketQo.getQuantity())
                .setOriginalPrice(skuStock.getPrice())
                .setPrice(skuStock.getPrice())
                .setProductPic(skuStock.getPic())
                .setProductName(product.getProductName())
                .setProductSubTitle(product.getSubTitle())
                .setProductSkuCode(skuStock.getSkuCode())
                .setDeleteStatus(0)
                .setProductCategoryId(product.getProductCategoryId())
                .setProductSn(product.getProductSn())
                .setProductAttr(skuStock.getSpData())
                .setTotalPrice(skuStock.getPrice().multiply(BigDecimal.valueOf(basketQo.getQuantity())))
                .setCreateTime(DateUtils.getNowDate())
                .setUpdateTime(DateUtils.getNowDate());
        basketItemService.save(basketItem);
    }

    private void updateBasketItem(BasketItem basketItem, BasketQo basketQo) {
        SkuStock skuStock = skuStockService.getById(basketQo.getProductSkuId());
        if (Objects.isNull(skuStock)) {
            throw new CommonException("商品不存在");
        }
        // 合并之前数量，并取最新的单价计算总额
        Long quantity = basketItem.getQuantity().longValue() + basketQo.getQuantity().longValue();
        BigDecimal totalPrice = skuStock.getPrice().multiply(BigDecimal.valueOf(quantity));


        basketItemService.updateById(new BasketItem().setId(basketItem.getId())
                .setQuantity(quantity)
                .setTotalPrice(totalPrice)
                .setUpdateTime(DateUtils.getNowDate()));
    }
}
