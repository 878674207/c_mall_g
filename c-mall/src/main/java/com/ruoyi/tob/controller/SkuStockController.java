package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.tob.entity.SkuStock;
import com.ruoyi.tob.service.SkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sku")
@Api(value = "sku库存管理", tags = {"sku库存管理"})
public class SkuStockController {
    @Autowired
    private SkuStockService skuStockService;

    @ApiOperation("根据商品ID及sku编码模糊搜索sku库存")
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getList(@PathVariable Long pid, @RequestParam(value = "keyword", required = false) String keyword) {
        List<SkuStock> skuStockList = skuStockService.getList(pid, keyword);
        return AjaxResult.success(skuStockList);
    }

    @ApiOperation("批量更新sku库存信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult update(@RequestBody List<SkuStock> skuStockList) {
        skuStockService.update(skuStockList);
        return AjaxResult.success();
    }

    @ApiOperation("批量删除sku库存信息")
    @DeleteMapping("/del")
    @ResponseBody
    public AjaxResult update(@RequestBody String[] ids) {
        skuStockService.del(ids);
        return AjaxResult.success();
    }
}
