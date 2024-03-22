package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;


import com.ruoyi.toc.entity.EvaluateReply;
import com.ruoyi.toc.service.EvaluateReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("c/evaluateReply")
@Api(value = "问一问回复信息", tags = {"问一问回复信息"})
@AllArgsConstructor
public class EvaluateReplyController {


    private final EvaluateReplyService evaluateReplyService;

    @ApiOperation(value = "回复分页查询", httpMethod = "POST")
    @PostMapping("index")
    public ResponseResult index(@RequestBody EvaluateReply evaluateReply, BaseQo page) {
        try {

            return ResponseResult.sucessResult(evaluateReplyService.index(evaluateReply, page));
        } catch (Exception e) {
            log.error("获取回复分页数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "保存回复", httpMethod = "POST")
    @PostMapping("saveReply")
    public ResponseResult saveReply(@RequestBody EvaluateReply evaluateReply) {
        try {
            evaluateReplyService.saveReply(evaluateReply);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存回复失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "删除我的回复", httpMethod = "POST")
    @PostMapping("delete/{replyId}")
    public ResponseResult deleteDeliveryAddress(@PathVariable Long replyId) {
        try {
            evaluateReplyService.del(replyId);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除我的回复失败", e);
            return ResponseResult.failResult(e.getMessage());
        }

    }

    @ApiOperation(value = "点赞评论", httpMethod = "GET")
    @GetMapping("like")
    public ResponseResult like(@RequestParam("replyId") Long replyId) {
        try {
            evaluateReplyService.like(replyId);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("点赞评论失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "取消点赞评论", httpMethod = "GET")
    @GetMapping("dontLike")
    public ResponseResult dontLike(@RequestParam("replyId") Long replyId) {
        try {
            evaluateReplyService.dontLike(replyId);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("取消点赞评论失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
