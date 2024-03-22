package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.StoreAttention;
import com.ruoyi.toc.service.StoreAttentionService;
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
@RequestMapping("c/store-attention")
@Api(value = "c端店铺关注管理", tags = {"c端店铺关注管理"})
public class StoreAttentionController {

    @Autowired
    private StoreAttentionService storeAttentionService;



    @ApiOperation("保存我的店铺关注")
    @PostMapping("/save")
    public ResponseResult saveMyStoreAttention(@RequestBody StoreAttention storeAttention){
        try {
            storeAttentionService.saveMyStoreAttention(storeAttention);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("保存我的店铺关注失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation("查询我的店铺关注列表")
    @PostMapping("/page-list")
    public ResponseResult queryMyStoreAttentionList(@RequestBody BaseQo baseQo) {
        try {
            return ResponseResult.sucessResult(storeAttentionService.queryMyStoreAttentionList(baseQo));
        } catch (Exception e) {
            log.error("查询我的店铺关注列表失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


    @ApiOperation("批量取消关注")
    @PostMapping("batch/cancel-attention")
    public ResponseResult batchCancelAttention(@RequestBody List<Long> ids) {
        try {
            storeAttentionService.batchCancelAttention(ids);
            return ResponseResult.sucessResult();
        } catch (Exception e) {
            log.error("批量取消关注失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }


}


