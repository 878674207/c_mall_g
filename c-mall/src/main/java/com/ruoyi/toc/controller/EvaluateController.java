package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;


import com.ruoyi.toc.entity.Evaluate;
import com.ruoyi.toc.service.EvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("c/evaluate")
@Api(value = "问一问信息", tags = {"问一问信息"})
@AllArgsConstructor
public class EvaluateController {


    private final EvaluateService evaluateService;


    @ApiOperation(value = "问一问分页查询", httpMethod = "POST")
    @PostMapping("index")
    public ResponseResult index(@RequestBody Evaluate evaluate, BaseQo page) {
        try {

            return ResponseResult.sucessResult(evaluateService.index(evaluate, page));
        } catch (Exception e) {
            log.error("获取问一问分页数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "保存提问", httpMethod = "POST")
    @PostMapping("save")
    public ResponseResult save(@RequestBody Evaluate evaluate) {
        try {
            evaluateService.saveQuestion(evaluate);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存提问失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "删除我的提问", httpMethod = "POST")
    @PostMapping("delete/{ids}")
    public ResponseResult deleteDeliveryAddress(@PathVariable Long ids) {
        try {
            evaluateService.del(ids);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("删除我的提问失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "关注问题", httpMethod = "GET")
    @GetMapping("careQuestion")
    public ResponseResult careQuestion(@RequestParam("questionId") Long questionId) {
        try {
            evaluateService.careQuestion(questionId);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("关注问题失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "取消关注问题", httpMethod = "GET")
    @GetMapping("dontCareQuestion")
    public ResponseResult dontCareQuestion(@RequestParam("questionId") Long questionId) {
        try {
            evaluateService.dontCareQuestion(questionId);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("取消关注问题失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "我关注的问题", httpMethod = "GET")
    @GetMapping("myCareQuestion")
    public ResponseResult myCareQuestion() {
        try {
            return ResponseResult.sucessResult(evaluateService.myCareQuestion());
        } catch (Exception e) {
            log.error("获取我关注的问题数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }
    @ApiOperation(value = "我的问题", httpMethod = "GET")
    @GetMapping("myQuestion")
    public ResponseResult myQuestion() {
        try {
            return ResponseResult.sucessResult(evaluateService.myQuestion());
        } catch (Exception e) {
            log.error("获取我的问题数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "我的回复", httpMethod = "GET")
    @GetMapping("myReply")
    public ResponseResult myReply() {
        try {
            return ResponseResult.sucessResult(evaluateService.myReply());
        } catch (Exception e) {
            log.error("获取我的回复数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "我的点赞", httpMethod = "GET")
    @GetMapping("myLike")
    public ResponseResult myLike() {
        try {
            return ResponseResult.sucessResult(evaluateService.myLike());
        } catch (Exception e) {
            log.error("获取我的回复数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }
    @ApiOperation(value = "根据questionId查询提问信息", httpMethod = "GET")
    @GetMapping("selectByQuestionId")
    public ResponseResult selectByQuestionId(Long questionId) {
        try {
            return ResponseResult.sucessResult(evaluateService.selectByQuestionId(questionId));
        } catch (Exception e) {
            log.error("获取提问信息数据失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

}
