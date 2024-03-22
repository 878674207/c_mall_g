package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductAttributeCategory;
import com.ruoyi.tob.service.ProductAttributeCategoryService;
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
@RequestMapping("b/product/attribute-category")
@Api(value = "b端商品属性分类管理", tags = {"b端商品属性分类管理"})
public class ProductAttributeCategoryController {

    @Autowired
    private ProductAttributeCategoryService productAttributeCategoryService;



    @ApiOperation(value = "保存商品属性分类", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult saveProductAttributeCategory(@Validated @RequestBody ProductAttributeCategory ProductAttributeCategory) {
        try {
            productAttributeCategoryService.saveProductAttributeCategory(ProductAttributeCategory);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存商品属性分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询商品属性分类", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryProductAttributeCategoryPageList(@RequestBody BaseQo baseQo) {
        try {
            return ResponseResult.sucessResult(productAttributeCategoryService.queryProductAttributeCategoryPageList(baseQo));
        } catch (Exception e) {
            log.error("分页查询商品属性分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }



    @ApiOperation(value = "根据id删除商品属性分类", httpMethod = "DELETE")
    @DeleteMapping("delete/{id}")
    public ResponseResult deleteProductAttributeCategory(@PathVariable("id") Long id) {
        try {
            productAttributeCategoryService.deleteProductAttributeCategory(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("根据id删除商品属性分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询所有商品属性分类", httpMethod = "GET")
    @GetMapping("all-list")
    public ResponseResult queryProductAttributeCategoryAllList() {
        try {
            return ResponseResult.sucessResult(productAttributeCategoryService.queryProductAttributeCategoryAllList());
        } catch (Exception e) {
            log.error("查询所有商品属性分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
