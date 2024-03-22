package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.AfterSales;
import com.ruoyi.toc.service.AfterSalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("c/after-sales")
@Api(value = "c端售后管理", tags = {"c端售后管理"})
public class AfterSalesController {

    @Autowired
    private AfterSalesService afterSalesService;

    @ApiOperation(value = "发起售后申请", httpMethod = "POST")
    @PostMapping("apply")
    public ResponseResult applyAfterSales(@RequestBody @Validated AfterSales afterSales) {
        try {
            afterSalesService.applyAfterSales(afterSales);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("发起售后申请失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "获取当前用户售后列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult<?> myAfterSalesList(@RequestBody BaseQo<AfterSales> baseQo) {
        try {
            return ResponseResult.sucessResult(afterSalesService.myAfterSalesList(baseQo));
        } catch (Exception e) {
            log.error("获取当前用户售后列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
