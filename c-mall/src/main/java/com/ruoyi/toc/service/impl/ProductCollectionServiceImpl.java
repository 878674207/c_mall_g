package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.mapper.ProductCategoryMapper;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.mapper.ProductCollectionMapper;
import com.ruoyi.toc.qo.ProductCollectionQo;
import com.ruoyi.toc.service.ProductCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductCollectionServiceImpl extends ServiceImpl<ProductCollectionMapper,ProductCollection> implements ProductCollectionService {

    @Autowired
    private ProductCollectionMapper productCollectionMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public void saveMyProductCollection(ProductCollection productCollection) {
        productCollection.setCustomerId(SecurityUtils.getCustomerLoginUser().getId());
        productCollection.setUpdateTime(DateUtils.getNowDate());
        productCollection.setCreateTime(DateUtils.getNowDate());
        productCollectionMapper.batchSaveProductCollection(Arrays.asList(productCollection));
    }

    @Override
    public Page queryMyProductCollectionList(ProductCollectionQo productCollectionQo) {
        Page page = productCollectionQo.initPage();
        productCollectionMapper.selectPage(page, new LambdaQueryWrapper<ProductCollection>()
                .eq(ProductCollection::getCustomerId, SecurityUtils.getCustomerLoginUser().getId())
                .eq(ProductCollection::getCollected, 1)
                .like(StringUtils.isNotEmpty(productCollectionQo.getKeyword()), ProductCollection::getProductName, productCollectionQo.getKeyword())
                .eq(Objects.nonNull(productCollectionQo.getProductCategoryId()), ProductCollection::getProductCategoryId, productCollectionQo.getProductCategoryId())
                .orderByDesc(ProductCollection::getUpdateTime));
        return page;
    }

    @Override
    public void batchCancelCollect(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<ProductCollection> list = Lists.newArrayList();
        ids.forEach(item -> list.add(new ProductCollection().setId(item).setCollected(0)));
        updateBatchById(list);
    }

    @Override
    public List<ProductCategory> queryProductCategory() {
        List<ProductCollection> productCollectionList = productCollectionMapper.selectList(new LambdaQueryWrapper<ProductCollection>()
                .eq(ProductCollection::getCustomerId, SecurityUtils.getCustomerLoginUser().getId())
                .eq(ProductCollection::getCollected, 1));
        if (CollectionUtils.isEmpty(productCollectionList)) {
            return Lists.newArrayList();
        }
        Set<Long> categoryIds = productCollectionList.stream().map(ProductCollection::getProductCategoryId).collect(Collectors.toSet());
        return productCategoryMapper.selectList(new QueryWrapper<ProductCategory>().select("id,category_name").in("id", categoryIds));
    }
}
