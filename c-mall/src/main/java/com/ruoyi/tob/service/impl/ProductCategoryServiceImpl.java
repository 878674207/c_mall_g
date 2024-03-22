package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.mapper.ProductCategoryMapper;
import com.ruoyi.tob.service.ProductCategoryService;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.converter.ProductCategoryConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Page queryProductCategoryPageList(BaseQo baseQo) {
        Page page = baseQo.initPage();
        productCategoryMapper.selectPage(page, new QueryWrapper<ProductCategory>()
                .eq("level", 0).orderByAsc("sort"));
        return page;
    }

    @Override
    public ProductCategory queryProductCategoryDetail(Long id) throws CommonException {
        ProductCategory productCategory = productCategoryMapper.selectById(id);
        if (Objects.isNull(productCategory)) {
            throw new CommonException("该商品分类不存在");
        }
        return productCategory;
    }

    @Override
    public void deleteProductCategory(Long id) throws CommonException {
        ProductCategory productCategory = productCategoryMapper.selectById(id);
        if (Objects.isNull(productCategory)) {
            throw new CommonException("该商品分类不存在");
        }
        if (!(productCategory.getLevel().intValue() == 1)) {
            List<ProductCategory> childCategory = productCategoryMapper.selectList(new QueryWrapper<ProductCategory>()
                    .eq("parent_id", id));
            if (CollectionUtils.isNotEmpty(childCategory)) {
                throw new CommonException("该商品分类存在子类分类，不允许删除");
            }
        }
        productCategoryMapper.deleteById(id);
    }

    @Override
    public void saveProductCategory(ProductCategory productCategory) {
        if (Objects.isNull(productCategory.getId())) {
            productCategoryMapper.insert(productCategory);
        } else {
            productCategoryMapper.updateById(productCategory);
        }
    }

    @Override
    public List<ProductCategory> queryProductCategoryList(Long parentId) {
        return productCategoryMapper.selectList(new QueryWrapper<ProductCategory>().eq("parent_id", parentId)
                .orderByAsc("sort"));
    }

    @Override
    public List<ProductCategoryVo> queryProductCategoryWithChildren() {
        return productCategoryMapper.queryProductCategoryWithChildren();
    }

    @Override
    public Object querycategoryTree() {
        List<ProductCategory> productCategories = productCategoryMapper.selectList(new QueryWrapper<ProductCategory>()
                .eq("show_status", 1).orderByAsc("sort"));
        if (CollectionUtils.isEmpty(productCategories)) {
            return Lists.newArrayList();
        }
        List<ProductCategoryVo> allProductCategoryVoList = ProductCategoryConverter.INSTANCE.toProductCategoryVoList(productCategories);
        List<ProductCategoryVo> result = allProductCategoryVoList
                .stream().filter(item -> item.getLevel().intValue() == 0).collect(Collectors.toList());
        result.forEach(item -> item.setChildren(allProductCategoryVoList.stream()
                .filter(subItem -> Objects.nonNull(subItem.getParentId()) && subItem.getParentId().equals(item.getId()))
                .collect(Collectors.toList())));
        return result;
    }

}
