package com.ruoyi.toc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.toc.entity.PurchaseOrder;
import com.ruoyi.toc.qo.PurchaseOrderItemQo;
import com.ruoyi.toc.qo.PurchaseOrderQo;
import com.ruoyi.toc.service.PurchaseOrderService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("c/purchase-order")
@Api(value = "c端采购单管理", tags = {"c端采购单管理"})
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @ApiOperation("保存采购单")
    @PostMapping("/save")
    public ResponseResult<?> savePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        try {
            purchaseOrderService.savePurchaseOrder(purchaseOrder);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存采购单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }



    @ApiOperation(value = "删除采购单", httpMethod = "DELETE")
    @DeleteMapping("delete/{id}")
    public ResponseResult<?> deletePurchaseOrder(@PathVariable("id") Long id) {
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除采购单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询我的采购单列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult<Page<PurchaseOrder>> queryPurchaseOrderList(@RequestBody PurchaseOrderQo purchaseOrderQo) {
        try {
            return ResponseResult.sucessResult(purchaseOrderService.queryPurchaseOrderList(purchaseOrderQo));
        } catch (Exception e) {
            log.error("查询我的采购单列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询我的采购单全部列表", httpMethod = "GET")
    @GetMapping("all-list")
    public ResponseResult<List<PurchaseOrder>> queryAllPurchaseOrderList() {
        try {
            return ResponseResult.sucessResult(purchaseOrderService.queryAllPurchaseOrderList());
        } catch (Exception e) {
            log.error("查询我的采购单全部列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询首页采购单", httpMethod = "GET")
    @GetMapping("home-page")
    public ResponseResult<PurchaseOrder> queryHomePagePurchaseOrder() {
        try {
            return ResponseResult.sucessResult(purchaseOrderService.queryHomePagePurchaseOrder());
        } catch (Exception e) {
            log.error("查询首页采购单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation("采购单添加商品")
    @PostMapping("/add-product")
    public ResponseResult<?> addProduct(@RequestBody @Validated PurchaseOrderItemQo purchaseOrderItemQo){
        try {
            purchaseOrderService.addProduct(purchaseOrderItemQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("采购单添加商品失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation("采购单修改商品")
    @PostMapping("/update-product")
    public ResponseResult<?> updateProduct(@RequestBody PurchaseOrderItemQo purchaseOrderItemQo){
        try {
            purchaseOrderService.updateProduct(purchaseOrderItemQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("采购单修改商品失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
