package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.entity.StoreInfo;
import com.ruoyi.tob.mapper.ProductCategoryMapper;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.tob.mapper.StoreMapper;
import com.ruoyi.tob.vo.ProductCategoryVo;
import com.ruoyi.toc.converter.StoreInfoConverter;
import com.ruoyi.toc.entity.StoreAttention;
import com.ruoyi.toc.mapper.OrderMapper;
import com.ruoyi.toc.mapper.StoreAttentionMapper;
import com.ruoyi.toc.service.StoreClientService;
import com.ruoyi.toc.vo.StoreInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StoreClientServiceImpl implements StoreClientService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StoreAttentionMapper storeAttentionMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public StoreInfoVo queryClientStoreDetail(Long id) {

        StoreInfo storeInfo = storeMapper.selectOne(new LambdaQueryWrapper<StoreInfo>()
                .select(StoreInfo::getId, StoreInfo::getStoreName, StoreInfo::getLogo, StoreInfo::getEntAddress)
                .eq(StoreInfo::getId, id));
        if (Objects.isNull(storeInfo)) {
            return null;
        }

        StoreInfoVo storeInfoVo = StoreInfoConverter.INSTANCE.toStoreInfoVo(storeInfo);

        // 粉丝数
        storeInfoVo.setFansCount(storeAttentionMapper.selectCount(new LambdaQueryWrapper<StoreAttention>()
                .eq(StoreAttention::getStoreId, storeInfoVo.getId())
                .eq(StoreAttention::getAttend, 1)));

        // 回头客数(在该店铺重复下单的人数)
        storeInfoVo.setRepeatCustomerCount(orderMapper.queryStoreRepeatCustomerCount(storeInfo.getId()));

        return storeInfoVo;
    }

    /**
     *  查询店铺推荐商品（前3名）
     * @param storeId 店铺id
     * @param type 0-> 销量榜； 1->收藏榜； 2->游览榜
     * @return 商品列表
     */
    @Override
    public Object queryStoreRecommend(Long storeId, String type) {
        if (Constants.ZERO.equals(type)) {
            return productMapper.selectList(new LambdaQueryWrapper<Product>().select(Product::getId,Product::getProductCategoryId,
                    Product::getProductName,Product::getPic, Product::getPrice,Product::getSale)
                    .eq(Product::getStoreId, storeId)
                    .eq(Product::getPublishStatus, 1)
                    .eq(Product::getApproveStatus, "approved")
                    .orderByDesc(Product::getSale)
                    .last("limit 3"));
        } else if (Constants.ONE.equals(type)) {
            return productMapper.queryMaxCollectedProductByStoreId(storeId);
        } else if (Constants.TWO.equals(type)) {
            return productMapper.selectList(new LambdaQueryWrapper<Product>().select(Product::getId,Product::getProductCategoryId,
                            Product::getProductName,Product::getPic, Product::getPrice,Product::getSale)
                    .eq(Product::getStoreId, storeId)
                    .eq(Product::getApproveStatus, "approved")
                    .eq(Product::getPublishStatus, 1)
                    .orderByDesc(Product::getVisitCount)
                    .last("limit 3"));
        }
        return null;
    }

    @Override
    public List<ProductCategoryVo> queryStoreProductCategory(Long storeId) {
        return productCategoryMapper.queryProductCategoryByChildrenIds(productMapper.queryProductCategoryIdByStoreId(storeId));
    }
}
