package com.ruoyi.toc.service;


import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.toc.entity.Customer;
import com.ruoyi.toc.qo.CustomerLoginQo;

public interface CustomerService {

    Result login(CustomerLoginQo customerLoginQo);
    Object sendSms(String phone);


    Customer getUserInfo();
}
