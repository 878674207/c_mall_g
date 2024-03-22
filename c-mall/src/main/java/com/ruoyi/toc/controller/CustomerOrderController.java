package com.ruoyi.toc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.Order;
import com.ruoyi.toc.entity.OrderAddress;
import com.ruoyi.toc.qo.OrderQo;
import com.ruoyi.toc.qo.OrderQueryQo;
import com.ruoyi.toc.service.CustomerOrderService;
import com.ruoyi.toc.vo.ConfirmOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("c/order")
@Api(value = "c端用户订单管理", tags = {"c端用户订单管理"})
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;


    @ApiOperation(value = "提交订单", httpMethod = "POST")
    @PostMapping("submit")
    public ResponseResult<?> submitOrder(@RequestBody @Validated OrderQo orderQo) {
        try {
            customerOrderService.submitOrder(orderQo);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("提交订单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "取消订单", httpMethod = "PUT")
    @PutMapping("cancel/{id}")
    public ResponseResult<?> cancelOrder(@PathVariable("id") Long id) {
        try {
            customerOrderService.cancelOrder(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "获取当前用户订单列表", httpMethod = "GET")
    @PostMapping("page-list")
    public ResponseResult<Page<Order>> myOrderList(@RequestBody OrderQueryQo orderQueryQo) {
        try {
            return ResponseResult.sucessResult(customerOrderService.myOrderList(orderQueryQo));
        } catch (Exception e) {
            log.error("获取当前用户订单列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "删除订单", httpMethod = "DELETE")
    @DeleteMapping("delete/{id}")
    public ResponseResult<?> deleteOrder(@PathVariable("id") Long id) {
        try {
            customerOrderService.deleteOrder(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除订单失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "修改订单地址", httpMethod = "POST")
    @PostMapping("address/save")
    public ResponseResult<?> updateAddress(@RequestBody OrderAddress orderAddress) {
        try {
            customerOrderService.updateAddress(orderAddress);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("修改订单地址失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询订单详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult queryOrderDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(customerOrderService.queryOrderDetail(id));
        } catch (Exception e) {
            log.error("查询订单详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "再来一单结算接口", httpMethod = "GET")
    @GetMapping("purchase-again/{id}")
    public ResponseResult<ConfirmOrderVo> purchaseAgain(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(customerOrderService.purchaseAgain(id));
        } catch (Exception e) {
            log.error("再来一单结算接口调用失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "确认收货", httpMethod = "PUT")
    @PutMapping("confirm-receive/{id}")
    public ResponseResult confirmReceive(@PathVariable("id") Long id) {
        try {
            customerOrderService.confirmReceive(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("确认收货失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
