package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.qo.BasketQo;
import com.ruoyi.toc.service.CustomerBasketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("c/basket")
@Api(value = "c端购物车管理", tags = {"c端购物车管理"})
public class CustomerBasketController {

    @Autowired
    private CustomerBasketService customerBasketService;


    @ApiOperation(value = "获取当前用户购物车列表", httpMethod = "GET")
    @PostMapping("list")
    public ResponseResult myBasketList(@RequestBody BaseQo baseQo) {
        try {
            return ResponseResult.sucessResult(customerBasketService.myBasketList(baseQo));
        } catch (Exception e) {
            log.error("获取当前用户购物车列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult saveBasket(@RequestBody BasketQo basketQo) {
        try {
            customerBasketService.saveBasket(basketQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("添加商品到购物车失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "删除购物车指定商品", httpMethod = "POST")
    @PostMapping("delete")
    public ResponseResult deleteBasket(@RequestBody BasketQo basketQo) {
        try {
            customerBasketService.deleteBasket(basketQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除购物车指定商品失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "清空购物车", httpMethod = "POST")
    @PostMapping("clear")
    public ResponseResult clearBasket() {
        try {
            customerBasketService.clearBasket();
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("清空购物车失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "购物车结算生成确认单", httpMethod = "POST")
    @PostMapping("settle")
    public ResponseResult settleBasket(@RequestBody List<BasketQo> basketQoList) {
        try {
            return ResponseResult.sucessResult(customerBasketService.settleBasket(basketQoList));
        } catch (Exception e) {
            log.error("购物车结算生成确认单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "购物车商品转移到收藏", httpMethod = "POST")
    @PostMapping("transfer-collection")
    public ResponseResult<?> transferCollection(@RequestBody BasketQo basketQo) {
        try {
            customerBasketService.transferCollection(basketQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("购物车商品转移到收藏失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
