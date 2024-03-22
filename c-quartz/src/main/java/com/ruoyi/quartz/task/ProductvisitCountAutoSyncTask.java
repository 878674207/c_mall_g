package com.ruoyi.quartz.task;


import com.ruoyi.common.constant.RedisConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("productvisitCountAutoSyncTask")
public class ProductvisitCountAutoSyncTask {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductServiceImpl productService;

    public void runProductvisitCountAutoSyncTask() {
        Collection<String> keys = redisCache.keys(RedisConstants.PRODUCT_VISIT_COUNT_PRE + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        Map<String, Number> visitCountMap = keys.stream().collect(Collectors.toMap(key -> key, key -> redisCache.getCacheObjectV2(key)));

        if (MapUtils.isEmpty(visitCountMap)) {
            return;
        }

        List<Product> updateList = Lists.newArrayList();
        visitCountMap.forEach((key, value) ->
            updateList.add(new Product().setId(Long.valueOf(key.substring(key.lastIndexOf(":")+1))).setVisitCount(value.longValue()))
        );

        productService.updateBatchById(updateList);

        redisCache.deleteObject(keys);
    }
}
