package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.entity.ProductAttribute;
import com.ruoyi.tob.qo.ProductAttributeQo;
import com.ruoyi.tob.service.ProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("b/product/attribute")
@Api(value = "b端商品属性管理", tags = {"b端商品属性管理"})
public class ProductAttributeController {

    @Autowired
    private ProductAttributeService productAttributeService;

    @ApiOperation(value = "分页查询商品属性", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryProductAttributePageList(@RequestBody ProductAttributeQo productAttributeQo) {
        try {
            return ResponseResult.sucessResult(productAttributeService.queryProductAttributePageList(productAttributeQo));
        } catch (Exception e) {
            log.error("分页查询商品属性失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "保存商品属性", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult saveProductAttribute(@Validated @RequestBody ProductAttribute productAttribute) {
        try {
            productAttributeService.saveProductAttribute(productAttribute);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存商品属性失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id删除商品属性", httpMethod = "DELETE")
    @DeleteMapping("delete/{id}")
    public ResponseResult deleteProductAttribute(@PathVariable("id") Long id) {
        try {
            productAttributeService.deleteProductAttribute(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("根据id删除商品属性失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询属性分类下所有属性和参数", httpMethod = "GET")
    @GetMapping("all-list/{id}")
    public ResponseResult queryProductAttributeAllList(@PathVariable("id") Long attributeCategoryId) {
        try {
            return ResponseResult.sucessResult(productAttributeService.queryProductAttributeAllList(attributeCategoryId));
        } catch (Exception e) {
            log.error("查询属性分类下所有属性和参数失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }
}
