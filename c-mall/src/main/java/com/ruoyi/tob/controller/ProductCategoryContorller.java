package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.service.ProductCategoryService;
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
@RequestMapping("b/product/category")
@Api(value = "b端商品分类管理", tags = {"b端商品分类管理"})
public class ProductCategoryContorller {
    @Autowired
    private ProductCategoryService productCategoryService;


    @ApiOperation(value = "保存商品分类", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult saveProductCategory(@Validated @RequestBody ProductCategory productCategory) {
        try {
            productCategoryService.saveProductCategory(productCategory);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存商品分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询商品分类", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryProductCategoryPageList(@RequestBody BaseQo baseQo) {
        try {
            return ResponseResult.sucessResult(productCategoryService.queryProductCategoryPageList(baseQo));
        } catch (Exception e) {
            log.error("分页查询商品分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询商品子分类", httpMethod = "GET")
    @GetMapping("list/{parentId}")
    public ResponseResult queryProductCategoryList(@PathVariable("parentId") Long parentId) {
        try {
            return ResponseResult.sucessResult(productCategoryService.queryProductCategoryList(parentId));
        } catch (Exception e) {
            log.error("查询商品子分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "根据id获取商品分类详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult queryProductCategoryDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(productCategoryService.queryProductCategoryDetail(id));
        } catch (Exception e) {
            log.error("根据id获取商品分类详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "根据id删除商品分类", httpMethod = "DELETE")
    @DeleteMapping("delete/{id}")
    public ResponseResult deleteProductCategory(@PathVariable("id") Long id) {
        try {
            productCategoryService.deleteProductCategory(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("根据id删除商品分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询所有一级分类及子分类", httpMethod = "GET")
    @GetMapping("with-children")
    public ResponseResult queryProductCategoryWithChildren() {
        try {
            return ResponseResult.sucessResult(productCategoryService.queryProductCategoryWithChildren());
        } catch (Exception e) {
            log.error("查询所有一级分类及子分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
