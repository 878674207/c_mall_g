package com.ruoyi.toc.controller;

import com.ruoyi.common.core.domain.ResponseResult;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.service.StoreClientService;
import com.ruoyi.toc.vo.StoreInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("c/store")
@Api(value = "c端店铺信息信息", tags = {"c端店铺信息信息"})
public class StoreClientController {

    @Autowired
    private StoreClientService storeClientService;

    @ApiOperation(value = "查询店铺详情", httpMethod = "GET")
    @GetMapping("detail/{id}")
    public ResponseResult<StoreInfoVo> queryClientStoreDetail(@PathVariable("id") Long id) {
        try {
            return ResponseResult.sucessResult(storeClientService.queryClientStoreDetail(id));
        } catch (Exception e) {
            log.error("查询店铺详情失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询店铺推荐榜", httpMethod = "GET")
    @GetMapping("recommend/{storeId}/{type}")
    public ResponseResult<?> queryStoreRecommend(@PathVariable("storeId") Long storeId,@PathVariable("type") String type) {
        try {
            return ResponseResult.sucessResult(storeClientService.queryStoreRecommend(storeId,type));
        } catch (Exception e) {
            log.error("查询店铺推荐榜失败", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }

    @ApiOperation(value = "查询店铺商品分类", httpMethod = "GET")
    @GetMapping("product-category/{id}")
    public ResponseResult<List<ProductCategoryVo>> queryStoreProductCategory(@PathVariable("id") Long storeId) {
        try {
            return ResponseResult.sucessResult(storeClientService.queryStoreProductCategory(storeId));
        } catch (Exception e) {
            log.error("查询店铺商品分类", e);
            return ResponseResult.failResult(e.getMessage());
        }
    }



}
