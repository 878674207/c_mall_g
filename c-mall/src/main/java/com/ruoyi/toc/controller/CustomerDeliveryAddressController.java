package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.DeliveryAddress;
import com.ruoyi.toc.service.CustomerDeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("c/address")
@Api(value = "c端用户收获地址管理", tags = {"c端用户收获地址管理"})
public class CustomerDeliveryAddressController {

    @Autowired
    private CustomerDeliveryAddressService customerDeliveryAddressService;



    @ApiOperation("保存收获地址")
    @PostMapping("/save")
    public ResponseResult saveDeliveryAddress(@RequestBody DeliveryAddress deliveryAddress){
        try {
            customerDeliveryAddressService.saveDeliveryAddress(deliveryAddress);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存收获地址失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }



    @ApiOperation(value = "删除收获地址", httpMethod = "POST")
    @PostMapping("delete")
    public ResponseResult deleteDeliveryAddress(@RequestBody List<Long> ids) {
        try {
            customerDeliveryAddressService.deleteDeliveryAddress(ids);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除收获地址失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询我的收获地址列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult queryDeliveryAddressList(@RequestBody BaseQo baseQo) {
        try {
            return ResponseResult.sucessResult(customerDeliveryAddressService.queryDeliveryAddressList(baseQo));
        } catch (Exception e) {
            log.error("查询我的收获地址列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
