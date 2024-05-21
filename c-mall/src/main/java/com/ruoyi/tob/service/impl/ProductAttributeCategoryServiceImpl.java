package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.constant.RoleConstants;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.tob.entity.ProductAttribute;
import com.ruoyi.tob.entity.ProductAttributeCategory;
import com.ruoyi.tob.mapper.ProductAttributeCategoryMapper;
import com.ruoyi.tob.mapper.ProductAttributeMapper;
import com.ruoyi.tob.service.ProductAttributeCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductAttributeCategoryServiceImpl implements ProductAttributeCategoryService {

    @Autowired
    private ProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Override
    public void saveProductAttributeCategory(ProductAttributeCategory productAttributeCategory) {
        if (Objects.isNull(productAttributeCategory.getId())) {
            productAttributeCategory.setStoreId(SecurityUtils.getStoreId());
            productAttributeCategoryMapper.insert(productAttributeCategory);
        } else {
            productAttributeCategoryMapper.updateById(productAttributeCategory);
        }
    }

    @Override
    public Page queryProductAttributeCategoryPageList(BaseQo baseQo) {
        Page page = baseQo.initPage();

        QueryWrapper<ProductAttributeCategory> queryWrapper = new QueryWrapper<>();

        // 店铺主管角色只能查看当前店铺的属性分类列表
        if (RoleConstants.MALL_STORE_MANAGER.equals(SecurityUtils.getLoginRoleCode())) {
            queryWrapper.eq("store_id", SecurityUtils.getStoreId());
        }
        productAttributeCategoryMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public void deleteProductAttributeCategory(Long id) {
        ProductAttributeCategory productAttributeCategory = productAttributeCategoryMapper.selectById(id);
        if (Objects.isNull(productAttributeCategory)) {
            throw new CommonException("该商品属性分类不存在");
        }
        productAttributeMapper.delete(new QueryWrapper<ProductAttribute>().eq("product_attribute_category_id", id));
        productAttributeCategoryMapper.deleteById(id);
    }

    @Override
    public List<ProductAttributeCategory> queryProductAttributeCategoryAllList() {
        QueryWrapper<ProductAttributeCategory> queryWrapper = new QueryWrapper<>();

        // 店铺主管角色只能查看当前店铺的属性分类列表
        if (RoleConstants.MALL_STORE_MANAGER.equals(SecurityUtils.getLoginRoleCode())) {
            queryWrapper.eq("store_id", SecurityUtils.getStoreId());
        }
        return productAttributeCategoryMapper.selectList(queryWrapper);
    }
}
