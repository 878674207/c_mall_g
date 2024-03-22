package com.ruoyi.tob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.entity.ProductAttribute;
import com.ruoyi.tob.entity.ProductAttributeCategory;
import com.ruoyi.tob.mapper.ProductAttributeCategoryMapper;
import com.ruoyi.tob.mapper.ProductAttributeMapper;
import com.ruoyi.tob.qo.ProductAttributeQo;
import com.ruoyi.tob.service.ProductAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private static final String ATTRIBUTE = "attribute";
    private static final String PARAMS = "params";

    private static final String INPUT = "input";


    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private ProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public Page queryProductAttributePageList(ProductAttributeQo productAttributeQo) {
        Page page = productAttributeQo.initPage();
        productAttributeMapper.selectPage(page, new QueryWrapper<ProductAttribute>()
                .eq("product_attribute_category_id", productAttributeQo.getProductAttributeCategoryId())
                .eq("type", productAttributeQo.getType())
                .orderByAsc("sort"));
        return page;
    }

    @Override
    public void saveProductAttribute(ProductAttribute productAttribute) throws CommonException {
        ProductAttributeCategory attributeCategory = productAttributeCategoryMapper.selectById(productAttribute.getProductAttributeCategoryId());
        if (Objects.isNull(attributeCategory)) {
            throw new CommonException("该属性分类不存在");
        }
        if (Objects.isNull(productAttribute.getId())) {
            insertProductAttribute(productAttribute, attributeCategory);
        } else {
            updateProductAttribute(productAttribute);
        }
    }

    private void insertProductAttribute(ProductAttribute productAttribute, ProductAttributeCategory attributeCategory) {
        if (PARAMS.equals(productAttribute.getType()) && INPUT.equals(productAttribute.getInputType())) {
            productAttribute.setHandAddStatus(null);
            productAttribute.setSelectType(null);
            productAttribute.setInputList(null);
        }
        productAttributeMapper.insert(productAttribute);
        if (ATTRIBUTE.equals(productAttribute.getType())) {
            attributeCategory.setAttributeCount(attributeCategory.getAttributeCount() + 1 );
        } else if (PARAMS.equals(productAttribute.getType())) {
            attributeCategory.setParamCount(attributeCategory.getParamCount() + 1 );
        }
        productAttributeCategoryMapper.updateById(attributeCategory);
    }

    private void updateProductAttribute(ProductAttribute productAttribute) {
        productAttributeMapper.updateById(productAttribute);
        if (PARAMS.equals(productAttribute.getType()) && INPUT.equals(productAttribute.getInputType())) {
            productAttributeMapper.updateAttributeToNull(productAttribute.getId());
        }
    }

    /**
     * 删除改属性，并修改属性分类属性个数
     * @param id 属性id
     * @throws CommonException 业务异常
     */
    @Override
    public void deleteProductAttribute(Long id) throws CommonException {
        ProductAttribute productAttribute = productAttributeMapper.selectById(id);
        if (Objects.isNull(productAttribute)) {
            throw new CommonException("该属性不存在");
        }

        ProductAttributeCategory attributeCategory = productAttributeCategoryMapper.selectById(productAttribute.getProductAttributeCategoryId());
        if (Objects.isNull(attributeCategory)) {
            throw new CommonException("该属性分类不存在");
        }
        if (ATTRIBUTE.equals(productAttribute.getType())) {
            attributeCategory.setAttributeCount(attributeCategory.getAttributeCount() - 1 );
        } else if (PARAMS.equals(productAttribute.getType())) {
            attributeCategory.setParamCount(attributeCategory.getParamCount() - 1 );
        }
        productAttributeCategoryMapper.updateById(attributeCategory);
        productAttributeMapper.deleteById(id);
    }

    @Override
    public List<ProductAttribute> queryProductAttributeAllList(Long attributeCategoryId) {
        return productAttributeMapper.selectList(new QueryWrapper<ProductAttribute>()
                .eq("product_attribute_category_id", attributeCategoryId));
    }
}
