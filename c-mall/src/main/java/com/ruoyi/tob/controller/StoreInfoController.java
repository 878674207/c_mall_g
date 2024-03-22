package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.tob.service.StoreInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lis
 * @Description: 店铺管理(平台端)
 * @date 2023/12/27
 */

@Slf4j
@RestController
@RequestMapping("p/store")
@RequiredArgsConstructor
@Api(value = "店铺管理(平台端)", tags = {"店铺管理(平台端)"})
public class StoreInfoController {

    private final StoreInfoService storeInfoService;


    @ApiOperation(value = "分页查询", httpMethod = "GET")
    @GetMapping("page")
    public AjaxResult show(StoreInfo storeInfo, BaseQo page) {
        return AjaxResult.success(storeInfoService.show(storeInfo, page));

    }

    @ApiOperation(value = "添加店铺", httpMethod = "POST")
    @PostMapping("saveStore")
    public AjaxResult saveStore(@RequestBody StoreInfo storeInfo) {
        return AjaxResult.success(storeInfoService.saveStore(storeInfo));
    }

    @ApiOperation(value = "根据id查询详情", httpMethod = "GET")
    @GetMapping("selectById")
    public AjaxResult show(String id) {
        return AjaxResult.success(storeInfoService.selectById(id));
    }

    @ApiOperation(value = "更改店铺状态", httpMethod = "GET")
    @GetMapping("forbid")
    public AjaxResult forbid(String id, String storeStatus, String forbidReason) {
        storeInfoService.forbid(id, storeStatus, forbidReason);
        return AjaxResult.success();
    }
}
