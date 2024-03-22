package com.ruoyi.tob.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.entity.AfterSalesRemarkLog;
import com.ruoyi.tob.qo.AfterSalesQo;
import com.ruoyi.tob.vo.AfterSalesVo;
import com.ruoyi.toc.service.AfterSalesService;
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
@RequestMapping("b/after-sales")
@Api(value = "b端售后管理", tags = {"b端售后管理"})
public class AfterSalesManageController {

    @Autowired
    private AfterSalesService afterSalesService;

    @ApiOperation(value = "获取当前用户店铺的售后列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult<PageInfo> queryAfterSalesList(@RequestBody AfterSalesQo afterSalesQo) {
        try {
            return ResponseResult.sucessResult(new PageInfo(afterSalesService.queryAfterSalesList(afterSalesQo)));
        } catch (Exception e) {
            log.error("获取当前用户店铺的售后列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询售后详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult<AfterSalesVo> queryAfterSalesDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(afterSalesService.queryAfterSalesDetail(id));
        } catch (Exception e) {
            log.error("查询售后详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "处理当前售后", httpMethod = "POST")
    @PostMapping("deal")
    public ResponseResult<?> dealAfterSales(@RequestBody AfterSalesQo afterSalesQo) {
        try {
            afterSalesService.dealAfterSales(afterSalesQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("处理当前售后失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "添加售后备注", httpMethod = "POST")
    @PostMapping("add/remark")
    public ResponseResult addAfterSalesRemark(@RequestBody AfterSalesRemarkLog afterSalesRemarkLog) {
        try {
            afterSalesService.addAfterSalesRemark(afterSalesRemarkLog);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("添加售后备注失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


}
