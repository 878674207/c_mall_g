package com.ruoyi.tob.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.entity.OrderRemarkLog;
import com.ruoyi.tob.qo.OrderQo;
import com.ruoyi.tob.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("b/order")
@Api(value = "b端订单管理", tags = {"b端订单管理"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "获取订单列表", httpMethod = "POST")
    @PostMapping("page-list")
    public ResponseResult<?> queyOrderList(@RequestBody OrderQo orderQo) {
        try {
            return ResponseResult.sucessResult(new PageInfo(orderService.queyOrderList(orderQo)));
        } catch (Exception e) {
            log.error("获取订单列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "查询订单详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult queryOrderDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(orderService.queryOrderDetail(id));
        } catch (Exception e) {
            log.error("查询订单详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "添加订单备注", httpMethod = "POST")
    @PostMapping("add/remark")
    public ResponseResult addOrderRemark(@RequestBody OrderRemarkLog orderRemarkLog) {
        try {
            orderService.addOrderRemark(orderRemarkLog);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("添加订单备注失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation(value = "确认发货", httpMethod = "PUT")
    @PutMapping("confirm-delivery/{id}")
    public ResponseResult confirmDelivery(@PathVariable("id") Long id) {
        try {
            orderService.confirmDelivery(id);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("确认发货失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
