package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.ApproveRequest;
import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.qo.ProductQo;
import com.ruoyi.tob.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("b/product")
@Api(value = "b端商品管理", tags = {"b端商品管理"})
public class ProductController {

    @Autowired
    private ProductService productService;


    @ApiOperation(value = "保存商品基本信息", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult saveProductInfo(@Validated @RequestBody ProductQo productQo) {
        try {
            productService.saveProductInfo(productQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存商品属性分类失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询商品信息", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryProductList(@RequestBody ProductQo productQo) {
        try {
            return ResponseResult.sucessResult(productService.queryProductList(productQo));
        } catch (Exception e) {
            log.error("分页查询商品信息失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "生成货号", httpMethod = "GET")
    @GetMapping("sn/generate")
    public ResponseResult generateSn() {
        try {
            return ResponseResult.sucessResult(productService.generateSn());
        } catch (Exception e) {
            log.error("生成货号失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询商品详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult queryProductDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(productService.queryProductDetail(id));
        } catch (Exception e) {
            log.error("生成货号失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }



    @ApiOperation(value = "批量操作", httpMethod = "POST")
    @PostMapping("batch-operate")
    public ResponseResult batchOperate(@RequestBody ProductQo productQo) {
        try {
            productService.batchOperate(productQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("批量操作商品信息失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "商品审核", httpMethod = "POST")
    @PostMapping("/approve")
    public ResponseResult productApprove(@RequestBody ApproveRequest approveRequest)  {
        try {
            productService.productApprove(approveRequest);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("商品审核失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
