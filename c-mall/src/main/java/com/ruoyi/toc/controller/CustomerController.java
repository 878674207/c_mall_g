package com.ruoyi.toc.controller;


import com.ruoyi.common.core.controller.BaseController;

import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.ResultStatusCode;
import com.ruoyi.toc.qo.CustomerLoginQo;
import com.ruoyi.toc.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName WechatUserController
 * @Description TOD0
 * author axx
 * Date 2023/8/15 10:09
 * Version 1.0
 **/
@RestController
@RequestMapping("/customer")
@Slf4j
@Api(value = "APP用户管理", tags = {"APP用户管理"})
public class CustomerController extends BaseController {
    @Resource
    private CustomerService customerService;


    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @PostMapping("login")
    public Object login(@Validated @RequestBody CustomerLoginQo customerLoginQo) {
        return customerService.login(customerLoginQo);
    }


    @ApiOperation(value = "接收短信验证码接口", httpMethod = "GET")
    @GetMapping("send-sms")
    public Object sendSms(@RequestParam("phone") String phone) {
        return customerService.sendSms(phone);
    }


    @GetMapping("user-info")
    @ApiOperation(value = "查询当前用户信息", httpMethod = "GET")
    public Result getUserInfo() {
        try {
            return Result.sucessResult(customerService.getUserInfo());
        } catch (Exception e) {
            log.error("查询当前用户信息失败", e);
            return Result.failResult(ResultStatusCode.SYSTEM_ERR.getErrorCode(), e.getMessage());
        }
    }


}
