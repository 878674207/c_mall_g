package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.tob.mapper.SkuStockMapper;
import com.ruoyi.tob.qo.SkuStockQo;
import com.ruoyi.tob.service.SkuStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

    private final String PRE_SKU_STOCK = "pre_sku_stock";

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<SkuStock> getList(Long pid, String keyword) {
        LambdaUpdateWrapper<SkuStock> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SkuStock::getProductId, pid)
                .like(StringUtils.isNotEmpty(keyword), SkuStock::getSkuCode, keyword);
        return list(wrapper);
    }

    @Override
    public void update(List<SkuStock> skuStockList) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        List<Long> skuStockIds = skuStockList.stream().map(SkuStock::getId).collect(Collectors.toList());
        List<SkuStock> stockList = list(new LambdaQueryWrapper<SkuStock>().in(SkuStock::getId, skuStockIds));
        if (skuStockList.size() != stockList.size()) {
            throw new CommonException("数据异常");
        }
        Map<Long, Long> lockMap = stockList.stream().collect(Collectors.toMap(SkuStock::getId, SkuStock::getLockStock));
        for(SkuStock item : skuStockList) {
            if (item.getStock().compareTo(lockMap.get(item.getId())) < 0) {
                throw new CommonException("sku为"+item.getSkuCode()+"商品，修改后库存不能小于锁定库存");
            }
        }
        saveOrUpdateBatch(skuStockList);

        // 修改商品主表的库存数量
        long totalStock = skuStockList.stream().mapToLong(SkuStock::getStock).sum();
        productMapper.updateById(new Product().setId(skuStockList.get(0).getProductId()).setStock(totalStock));

    }

    @Override
    public void del(String[] ids) {
        removeByIds(Arrays.asList(ids));
    }


    /**
     * 锁定库存
     *
     * @param skuStockQoList
     */
    @Override
    public void lockSkuStock(List<SkuStockQo> skuStockQoList) {
        if (CollectionUtils.isEmpty(skuStockQoList)) {
            return;
        }
        RLock multiLock = redissonClient.getMultiLock(skuStockQoList.stream().map(
                item -> redissonClient.getLock(PRE_SKU_STOCK + item.getProductSkuId())
        ).toArray(RLock[]::new));


        try {
            // 非阻塞
            if (multiLock.tryLock(3, TimeUnit.SECONDS)) {

                // 获取组合锁成功
                try {
                    lockSkuStockHandler(skuStockQoList);
                } finally {
                    // 解锁
                    multiLock.unlock();
                }

            } else {
                // 获取组合锁失败
                throw new CommonException("请稍后重试");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void lockSkuStockHandler(List<SkuStockQo> skuStockQoList) {
        Map<Long, SkuStock> stockMap = getStockMap(skuStockQoList);
        if (MapUtils.isEmpty(stockMap)) {
            return;
        }

        List<SkuStock> udpateList = Lists.newArrayList();
        for (SkuStockQo item : skuStockQoList) {
            SkuStock skuStock = stockMap.get(item.getProductSkuId());
            if (Objects.isNull(skuStock)) {
                continue;
            }
            if ((skuStock.getLockStock().longValue() + item.getLockQuantity().longValue()) > skuStock.getStock()) {
                throw new CommonException("商品" + skuStock.getSkuCode() + "库存不足");
            }
            udpateList.add(new SkuStock().setId(skuStock.getId()).setLockStock(skuStock.getLockStock().longValue() + item.getLockQuantity().longValue()));
        }

        if (CollectionUtils.isEmpty(udpateList)) {
            return;
        }
        updateBatchById(udpateList);

    }


    /**
     * 解锁库存
     *
     * @param skuStockQoList
     */
    @Override
    public void unLockSkuStock(List<SkuStockQo> skuStockQoList) {
        if (CollectionUtils.isEmpty(skuStockQoList)) {
            return;
        }
        RLock multiLock = redissonClient.getMultiLock(skuStockQoList.stream().map(
                item -> redissonClient.getLock(PRE_SKU_STOCK + item.getProductSkuId())
        ).toArray(RLock[]::new));

        try {
            // 非阻塞
            if (multiLock.tryLock(3, TimeUnit.SECONDS)) {

                // 获取组合锁成功
                try {
                    unLockSkuStockHandler(skuStockQoList);
                } finally {
                    // 解锁
                    multiLock.unlock();
                }

            } else {
                // 获取组合锁失败
                throw new CommonException("请稍后重试");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    private void unLockSkuStockHandler(List<SkuStockQo> skuStockQoList) {

        Map<Long, SkuStock> stockMap = getStockMap(skuStockQoList);
        if (MapUtils.isEmpty(stockMap)) {
            return;
        }
        List<SkuStock> udpateList = Lists.newArrayList();
        for (SkuStockQo item : skuStockQoList) {
            SkuStock skuStock = stockMap.get(item.getProductSkuId());
            if (Objects.isNull(skuStock)) {
                continue;
            }
            if (skuStock.getLockStock().longValue() < item.getUnlockQuantity().longValue()) {
                throw new CommonException("商品" + skuStock.getSkuCode() + "解锁数量异常");
            }
            udpateList.add(new SkuStock().setId(skuStock.getId()).setLockStock(skuStock.getLockStock().longValue() - item.getUnlockQuantity().longValue()));
        }

        if (CollectionUtils.isEmpty(udpateList)) {
            return;
        }
        updateBatchById(udpateList);
    }


    /**
     * 扣减库存
     *
     * @param skuStockQoList
     */
    @Override
    public void reduceSkuStock(List<SkuStockQo> skuStockQoList) {
        if (CollectionUtils.isEmpty(skuStockQoList)) {
            return;
        }

        RLock multiLock = redissonClient.getMultiLock(skuStockQoList.stream().map(
                item -> redissonClient.getLock(PRE_SKU_STOCK + item.getProductSkuId())
        ).toArray(RLock[]::new));

        try {
            // 非阻塞
            if (multiLock.tryLock(3, TimeUnit.SECONDS)) {

                // 获取组合锁成功
                try {
                    reduceSkuStockHandler(skuStockQoList);
                } finally {
                    // 解锁
                    multiLock.unlock();
                }

            } else {
                // 获取组合锁失败
                throw new CommonException("请稍后重试");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void reduceSkuStockHandler(List<SkuStockQo> skuStockQoList) {
        Map<Long, SkuStock> stockMap = getStockMap(skuStockQoList);
        if (MapUtils.isEmpty(stockMap)) {
            return;
        }
        List<SkuStock> udpateList = Lists.newArrayList();
        for (SkuStockQo item : skuStockQoList) {
            SkuStock skuStock = stockMap.get(item.getProductSkuId());
            if (Objects.isNull(skuStock)) {
                continue;
            }

            udpateList.add(new SkuStock().setId(skuStock.getId())
                    .setLockStock(skuStock.getLockStock().longValue() - item.getLockQuantity().longValue())
                    .setStock(skuStock.getStock().longValue() - item.getLockQuantity().longValue()));
        }

        if (CollectionUtils.isEmpty(udpateList)) {
            return;
        }
        updateBatchById(udpateList);
    }

    private Map<Long, SkuStock> getStockMap(List<SkuStockQo> skuStockQoList) {
        List<SkuStock> stockList = list(new LambdaQueryWrapper<SkuStock>().in(SkuStock::getId,
                skuStockQoList.stream().map(SkuStockQo::getProductSkuId).collect(Collectors.toList())));
        if (CollectionUtils.isEmpty(stockList)) {
            return Maps.newHashMap();
        }
        return stockList.stream().collect(Collectors.toMap(SkuStock::getId, Function.identity()));
    }

}
