package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.SecurityUtils;

import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.toc.entity.*;
import com.ruoyi.toc.mapper.*;
import com.ruoyi.toc.service.EvaluateReplyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Lis
 * @Description:
 * @date 2024/1/30
 */
@AllArgsConstructor
@Service
public class EvaluateReplyServiceImpl extends ServiceImpl<EvaluateReplyMapper, EvaluateReply> implements EvaluateReplyService {
    private final CustomerMapper wechatMapper;
    private final EvaluateLikeMapper evaluateLikeMapper;
    private final ProductMapper productMapper;
    private final EvaluateMapper evaluateMapper;

    @Override
    public Page<EvaluateReply> index(EvaluateReply evaluateReply, BaseQo page) {
        Evaluate one = evaluateMapper.selectById(evaluateReply.getQuestionId());
        LambdaQueryWrapper<Product> productLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productLambdaQueryWrapper.eq(Product::getId,one.getProductId());
        Product product = productMapper.selectOne(productLambdaQueryWrapper);

        LambdaQueryWrapper<EvaluateReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(evaluateReply.getQuestionId() != null, EvaluateReply::getQuestionId, evaluateReply.getQuestionId());
        Page initPage = page.initPage();
        Page<EvaluateReply> result = page(initPage, wrapper);
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        Map<Long, List<EvaluateLike>> likeMap = evaluateLikeMapper.selectList(null).stream().collect(Collectors.groupingBy(EvaluateLike::getReplyId));
        //我是否点赞回复
        LambdaQueryWrapper<EvaluateLike> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(EvaluateLike::getLikeUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateLike>> myLikeMap = evaluateLikeMapper.selectList(likeWrapper).stream().collect(Collectors.groupingBy(EvaluateLike::getReplyId));
        for (EvaluateReply record : result.getRecords()) {
            //是否匿名
            if (record.getIsAnonymous() == 1) {
                userMap.get(record.getReplyUserId()).get(0).setNickname("匿名用户");
                record.setReplyUserInfo(userMap.get(record.getReplyUserId()).get(0));
            } else record.setReplyUserInfo(userMap.get(record.getReplyUserId()).get(0));
            //我是否点赞
            if (myLikeMap.get(record.getId()) != null) record.setIsLike(new Long(1));
            else record.setIsLike(new Long(0));
            //设置回复数量
            Long likeNumber;
            likeNumber=likeMap.get(record.getId())==null?new Long(0):new Long(likeMap.get(record.getId()).size());
            record.setLikeNumber(likeNumber);
            //设置评论者购买信息
            record.setProductName(product.getProductName());
        }
        result.getRecords().stream().sorted(Comparator.comparing(EvaluateReply::getLikeNumber).reversed());
        return result;
    }

    @Override
    public void saveReply(EvaluateReply evaluateReply) {
        evaluateReply.setReplyUserId(SecurityUtils.getCustomerLoginUserId());
        evaluateReply.setCreateTime(new Date());
        save(evaluateReply);
    }


    @Override
    public void del(Long id) {
        removeById(id);
        LambdaQueryWrapper<EvaluateLike> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(EvaluateLike::getReplyId,id);
        evaluateLikeMapper.delete(likeWrapper);
    }

    @Override
    public void like(Long replyId) {
        EvaluateLike like = new EvaluateLike();
        like.setLikeUserId(SecurityUtils.getCustomerLoginUserId());
        like.setReplyId(replyId);
        like.setCreateTime(new Date());
        evaluateLikeMapper.insert(like);
    }

    @Override
    public void dontLike(Long replyId) {
        LambdaQueryWrapper<EvaluateLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluateLike::getReplyId, replyId)
                .eq(EvaluateLike::getLikeUserId, SecurityUtils.getCustomerLoginUserId());
        evaluateLikeMapper.delete(wrapper);
    }
}
