package com.ruoyi.common.controller;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.request.LocationRequest;
import com.ruoyi.common.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("custom/location")
@Api("地理信息查询")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ApiOperation("地理信息按层级查询接口")
    @PostMapping("/query")
    public Result queryLocationInfo(@RequestBody LocationRequest locationRequest) {
        return new Result<>(locationService.queryLocationInfo(locationRequest));
    }
}
