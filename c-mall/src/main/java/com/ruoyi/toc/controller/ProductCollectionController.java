package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.entity.ProductCategory;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.entity.ProductCollection;
import com.ruoyi.toc.qo.ProductCollectionQo;
import com.ruoyi.toc.service.ProductCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("c/product-collection")
@Api(value = "c端商品收藏管理", tags = {"c端商品收藏管理"})
public class ProductCollectionController {

    @Autowired
    private ProductCollectionService productCollectionService;



    @ApiOperation("保存我的收藏商品")
    @PostMapping("/save")
    public ResponseResult saveMyProductCollection(@RequestBody ProductCollection productCollection){
        try {
            productCollectionService.saveMyProductCollection(productCollection);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存我的收藏商品失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation("查询我的商品收藏列表")
    @PostMapping("/page-list")
    public ResponseResult queryMyProductCollectionList(@RequestBody ProductCollectionQo productCollectionQo) {
        try {
            return ResponseResult.sucessResult(productCollectionService.queryMyProductCollectionList(productCollectionQo));
        } catch (Exception e) {
            log.error("查询我的商品收藏列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation("批量取消收藏")
    @PostMapping("batch/cancel-collect")
    public ResponseResult batchCancelCollect(@RequestBody ProductCollectionQo productCollectionQo) {
        try {
            productCollectionService.batchCancelCollect(productCollectionQo.getIds());
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("批量取消收藏失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询商品分类", httpMethod = "GET")
    @GetMapping("product-category")
    public ResponseResult<List<ProductCategory>> queryProductCategory() {
        try {
            return ResponseResult.sucessResult(productCollectionService.queryProductCategory());
        } catch (Exception e) {
            log.error("查询商品分类", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }
}
