package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.service.ProductCategoryService;
import com.ruoyi.tob.service.ProductService;
import com.ruoyi.toc.qo.ClientProductQo;
import com.ruoyi.toc.qo.SettleQo;
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

@Slf4j
@RestController
@RequestMapping("c/product")
@Api(value = "c端商品信息", tags = {"c端商品信息"})
public class ProductClientController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;


    @ApiOperation(value = "查询显示状态正常的品类树", httpMethod = "GET")
    @GetMapping("category-tree")
    public ResponseResult querycategoryTree() {
        try {
            return ResponseResult.sucessResult(productCategoryService.querycategoryTree());
        } catch (Exception e) {
            log.error("查询显示状态正常的品类树失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询商品列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryClientProductList(@RequestBody ClientProductQo clientProductQo) {
        try {
            return ResponseResult.sucessResult(productService.queryClientProductList(clientProductQo));
        } catch (Exception e) {
            log.error("查询显示状态正常的品类树失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询商品详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult queryClientProductDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(productService.queryClientProductDetail(id));
        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "商品详情立即结算", httpMethod = "POST")
    @PostMapping("settle")
    public ResponseResult productSettle(@RequestBody SettleQo settleQo) {
        try {
            return ResponseResult.sucessResult(productService.productSettle(settleQo));
        } catch (Exception e) {
            log.error("商品详情立即结算失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }
}
