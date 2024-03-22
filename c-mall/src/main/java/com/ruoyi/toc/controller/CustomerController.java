package com.ruoyi.toc.controller;


import com.ruoyi.common.core.controller.BaseController;

import com.ruoyi.common.core.domain.CommonException;
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
@Api(value = "微信小程序用户管理", tags = {"微信小程序用户管理"})
public class CustomerController extends BaseController {
    @Resource
    private CustomerService wechatService;




    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @PostMapping("login")
    public Object login(@Validated @RequestBody CustomerLoginQo customerLoginQo) throws CommonException {
        return wechatService.login(customerLoginQo);
    }


    @ApiOperation(value = "接收短信验证码接口", httpMethod = "GET")
    @GetMapping("sendSms")
    public Object sendSms(@RequestParam("phone") String phone) {
        return wechatService.sendSms(phone);
    }




}
