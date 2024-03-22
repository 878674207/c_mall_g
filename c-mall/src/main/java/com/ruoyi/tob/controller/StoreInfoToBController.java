package com.ruoyi.tob.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.tob.service.StoreInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lis
 * @Description: 店铺管理(商家端)
 * @date 2023/12/27
 */
@Slf4j
@RestController
@RequestMapping("b/store")
@RequiredArgsConstructor
@Api(value = "店铺管理(商户端)", tags = {"店铺管理(商户端)"})
public class StoreInfoToBController {
    private final StoreInfoService storeInfoService;

    @ApiOperation(value = "修改店铺信息", httpMethod = "POST")
    @PostMapping("updateStore")
    public AjaxResult updateStore(@RequestBody StoreInfo storeInfo) {
        storeInfoService.updateStore(storeInfo);
        return AjaxResult.success();
    }
    @ApiOperation(value = "当前店铺信息", httpMethod = "GET")
    @GetMapping("currentStoreInfo")
    public AjaxResult currentStoreInfo() {
        return AjaxResult.success(storeInfoService.currentStoreInfo());
    }
}
