package com.ruoyi.toc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.tob.entity.Product;
import com.ruoyi.tob.mapper.ProductMapper;
import com.ruoyi.toc.entity.*;
import com.ruoyi.toc.mapper.*;
import com.ruoyi.toc.service.EvaluateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lis
 * @Description:
 * @date 2024/1/30
 */
@AllArgsConstructor
@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {
    private final CustomerMapper wechatMapper;
    private final EvaluateCareMapper evaluateCareMapper;
    private final EvaluateReplyMapper evaluateReplyMapper;
    private final OrderMapper orderMapper;
    private final EvaluateLikeMapper evaluateLikeMapper;
    private final ProductMapper productMapper;

    @Override
    public void saveQuestion(Evaluate evaluate) {
        evaluate.setUserId(SecurityUtils.getCustomerLoginUserId());
        evaluate.setCreateTime(new Date());
        save(evaluate);
    }

    @Override
    public Page<Evaluate> index(Evaluate evaluate, BaseQo page) {
        LambdaQueryWrapper<Evaluate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(evaluate.getProductId() != null, Evaluate::getProductId, evaluate.getProductId())
                .like(StringUtils.isNotEmpty(evaluate.getQuestionContent()), Evaluate::getQuestionContent, evaluate.getQuestionContent());
        Page initPage = page.initPage();
        Page<Evaluate> result = page(initPage, wrapper);
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        //我是否关注问题
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateCare>> careMap = evaluateCareMapper.selectList(careWrapper).stream().collect(Collectors.groupingBy(EvaluateCare::getQuestionId));
        //首条回复
        Map<Long, List<EvaluateReply>> replyMap = evaluateReplyMapper.selectList(null).stream().collect(Collectors.groupingBy(EvaluateReply::getQuestionId));
        //当前人是否可以回复
        Map<Long, List<Order>> orderMap = orderMapper.selectList(null).stream().collect(Collectors.groupingBy(Order::getCustomerId));

        for (Evaluate record : result.getRecords()) {
            //是否匿名
            if (record.getIsAnonymous() == 1) {
                userMap.get(record.getUserId()).get(0).setNickname("匿名用户");
                record.setUserInfo(userMap.get(record.getUserId()).get(0));
            } else record.setUserInfo(userMap.get(record.getUserId()).get(0));
            //当前人是否关注问题
            if (careMap.get(record.getId()) != null) record.setIsCare(new Long(1));
            else record.setIsCare(new Long(0));
            //设置首条回复
            if (replyMap.get(record.getId()) != null) {
                replyMap.get(record.getId()).stream().sorted(Comparator.comparing(EvaluateReply::getLikeNumber).reversed());
                record.setFirstReply(replyMap.get(record.getId()).get(0));
            }
            //设置回复数量
            Long replyNumber;
            replyNumber = replyMap.get(record.getId()) == null ? new Long(0) : new Long(replyMap.get(record.getId()).size());
            record.setReplyNumber(replyNumber);
            //当前人是否可以回复
            if (orderMap.get(SecurityUtils.getCustomerLoginUserId()) != null) {
                for (Order order : orderMap.get(SecurityUtils.getCustomerLoginUserId())) {
                    if (order.getId() == record.getProductId() && "completed".equals(order.getOrderStatus()))
                        record.setCanReply(new Long(0));
                    else record.setCanReply(new Long(1));
                }
            } else record.setCanReply(new Long(1));
        }
        return result;
    }

    @Override
    public void del(Long id) {
        removeById(id);
        LambdaQueryWrapper<EvaluateReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluateReply::getQuestionId, id);
        evaluateReplyMapper.delete(wrapper);
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getQuestionId, id);
        evaluateCareMapper.delete(careWrapper);
    }

    @Override
    public void careQuestion(Long questionId) {
        EvaluateCare one = new EvaluateCare();
        one.setCareUserId(SecurityUtils.getCustomerLoginUserId());
        one.setQuestionId(questionId);
        one.setCreateTime(new Date());
        evaluateCareMapper.insert(one);
    }

    @Override
    public void dontCareQuestion(Long questionId) {
        LambdaQueryWrapper<EvaluateCare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId())
                .eq(EvaluateCare::getQuestionId, questionId);
        evaluateCareMapper.delete(wrapper);
    }

    @Override
    public List<Evaluate> myCareQuestion() {
        LambdaQueryWrapper<EvaluateCare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId());
        List<Long> questionIds = evaluateCareMapper.selectList(wrapper).stream().map(EvaluateCare::getQuestionId).collect(Collectors.toList());
        List<Evaluate> list = new ArrayList<>();
        if (questionIds.size() > 0) {
            LambdaQueryWrapper<Evaluate> questionWrapper = new LambdaQueryWrapper<>();
            questionWrapper.in(Evaluate::getId, questionIds);
            list = list(questionWrapper);
        }
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        //我是否关注问题
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateCare>> careMap = evaluateCareMapper.selectList(careWrapper).stream().collect(Collectors.groupingBy(EvaluateCare::getQuestionId));
        //首条回复
        Map<Long, List<EvaluateReply>> replyMap = evaluateReplyMapper.selectList(null).stream().collect(Collectors.groupingBy(EvaluateReply::getQuestionId));
        //当前人是否可以回复
        Map<Long, List<Order>> orderMap = orderMapper.selectList(null).stream().collect(Collectors.groupingBy(Order::getCustomerId));

        for (Evaluate record : list) {
            //是否匿名
            if (record.getIsAnonymous() == 1) {
                userMap.get(record.getUserId()).get(0).setNickname("匿名用户");
                record.setUserInfo(userMap.get(record.getUserId()).get(0));
            } else record.setUserInfo(userMap.get(record.getUserId()).get(0));
            //当前人是否关注问题
            if (careMap.get(record.getId()) != null) record.setIsCare(new Long(1));
            else record.setIsCare(new Long(0));
            //设置首条回复
            if (replyMap.get(record.getId()) != null) {
                replyMap.get(record.getId()).stream().sorted(Comparator.comparing(EvaluateReply::getLikeNumber).reversed());
                record.setFirstReply(replyMap.get(record.getId()).get(0));
            }
            //设置回复数量
            Long replyNumber;
            replyNumber = replyMap.get(record.getId()) == null ? new Long(0) : new Long(replyMap.get(record.getId()).size());
            record.setReplyNumber(replyNumber);
            //当前人是否可以回复
            if (orderMap.get(SecurityUtils.getCustomerLoginUserId()) != null) {
                for (Order order : orderMap.get(SecurityUtils.getCustomerLoginUserId())) {
                    if (order.getId() == record.getProductId() && "completed".equals(order.getOrderStatus()))
                        record.setCanReply(new Long(0));
                    else record.setCanReply(new Long(1));
                }
            } else record.setCanReply(new Long(1));
        }
        return list;
    }

    @Override
    public List<Evaluate> myReply() {
        LambdaQueryWrapper<EvaluateReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EvaluateReply::getReplyUserId, SecurityUtils.getCustomerLoginUserId());
        List<Long> questionIds = evaluateReplyMapper.selectList(wrapper).stream().map(EvaluateReply::getQuestionId).collect(Collectors.toList());
        List<Evaluate> list = new ArrayList<>();
        if (questionIds.size() > 0) {
            LambdaQueryWrapper<Evaluate> questionWrapper = new LambdaQueryWrapper<>();
            questionWrapper.in(Evaluate::getId, questionIds);
            list = list(questionWrapper);
        }
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        //我的回复
        LambdaQueryWrapper<EvaluateReply> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(EvaluateReply::getReplyUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateReply>> replyMap = evaluateReplyMapper.selectList(replyWrapper).stream().collect(Collectors.groupingBy(EvaluateReply::getQuestionId));
        //我是否关注问题
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateCare>> careMap = evaluateCareMapper.selectList(careWrapper).stream().collect(Collectors.groupingBy(EvaluateCare::getQuestionId));
        //当前人是否可以回复
        Map<Long, List<Order>> orderMap = orderMapper.selectList(null).stream().collect(Collectors.groupingBy(Order::getCustomerId));

        for (Evaluate record : list) {
            //是否匿名
            if (record.getIsAnonymous() == 1) {
                userMap.get(record.getUserId()).get(0).setNickname("匿名用户");
                record.setUserInfo(userMap.get(record.getUserId()).get(0));
            } else record.setUserInfo(userMap.get(record.getUserId()).get(0));
            //我的回复
            for (Long questionId : replyMap.keySet()) {
                if (record.getId().equals(questionId)) record.setFirstReply(replyMap.get(questionId).get(0));
            }
            //当前人是否关注问题
            if (careMap.get(record.getId()) != null) record.setIsCare(new Long(1));
            else record.setIsCare(new Long(0));
            //设置回复数量
            Long replyNumber;
            replyNumber = replyMap.get(record.getId()) == null ? new Long(0) : new Long(replyMap.get(record.getId()).size());
            record.setReplyNumber(replyNumber);
            //当前人是否可以回复
            if (orderMap.get(SecurityUtils.getCustomerLoginUserId()) != null) {
                for (Order order : orderMap.get(SecurityUtils.getCustomerLoginUserId())) {
                    if (order.getId() == record.getProductId() && "completed".equals(order.getOrderStatus()))
                        record.setCanReply(new Long(0));
                    else record.setCanReply(new Long(1));
                }
            } else record.setCanReply(new Long(1));
        }

        return list;
    }

    @Override
    public List<Evaluate> myQuestion() {
        LambdaQueryWrapper<Evaluate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Evaluate::getUserId, SecurityUtils.getCustomerLoginUserId());
        List<Evaluate> list = list(wrapper);
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        //我是否关注问题
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId());
        Map<Long, List<EvaluateCare>> careMap = evaluateCareMapper.selectList(careWrapper).stream().collect(Collectors.groupingBy(EvaluateCare::getQuestionId));
        //首条回复
        Map<Long, List<EvaluateReply>> replyMap = evaluateReplyMapper.selectList(null).stream().collect(Collectors.groupingBy(EvaluateReply::getQuestionId));
        //当前人是否可以回复
        Map<Long, List<Order>> orderMap = orderMapper.selectList(null).stream().collect(Collectors.groupingBy(Order::getCustomerId));

        for (Evaluate record : list) {
            //是否匿名
            if (record.getIsAnonymous() == 1) {
                userMap.get(record.getUserId()).get(0).setNickname("匿名用户");
                record.setUserInfo(userMap.get(record.getUserId()).get(0));
            } else record.setUserInfo(userMap.get(record.getUserId()).get(0));
            //当前人是否关注问题
            if (careMap.get(record.getId()) != null) record.setIsCare(new Long(1));
            else record.setIsCare(new Long(0));
            //设置首条回复
            if (replyMap.get(record.getId()) != null) {
                replyMap.get(record.getId()).stream().sorted(Comparator.comparing(EvaluateReply::getLikeNumber).reversed());
                record.setFirstReply(replyMap.get(record.getId()).get(0));
            }
            //设置回复数量
            Long replyNumber;
            replyNumber = replyMap.get(record.getId()) == null ? new Long(0) : new Long(replyMap.get(record.getId()).size());
            record.setReplyNumber(replyNumber);
            //当前人是否可以回复
            if (orderMap.get(SecurityUtils.getCustomerLoginUserId()) != null) {
                for (Order order : orderMap.get(SecurityUtils.getCustomerLoginUserId())) {
                    if (order.getId() == record.getProductId() && "completed".equals(order.getOrderStatus()))
                        record.setCanReply(new Long(0));
                    else record.setCanReply(new Long(1));
                }
            } else record.setCanReply(new Long(1));
        }
        return list;
    }

    @Override
    public Evaluate selectByQuestionId(Long questionId) {
        LambdaQueryWrapper<Evaluate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Evaluate::getId, questionId);
        Evaluate one = getOne(wrapper);
        LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
        careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId())
                .eq(EvaluateCare::getQuestionId, questionId);
        Long careCount = evaluateCareMapper.selectCount(careWrapper);
        //设置是否关注问题
        if (careCount > 0) one.setIsCare(new Long(1));
        else one.setIsCare(new Long(0));
        //设置回复数量
        LambdaQueryWrapper<EvaluateReply> replyWrapper = new LambdaQueryWrapper<>();
        replyWrapper.eq(EvaluateReply::getQuestionId, questionId);
        one.setReplyNumber(evaluateReplyMapper.selectCount(replyWrapper));
        //设置匿名
        Customer customer = wechatMapper.selectById(SecurityUtils.getCustomerLoginUserId());
        if (one.getIsAnonymous()==1) {
            customer.setNickname("匿名用户");
        }
        //设置提问人信息
        one.setUserInfo(customer);
        return one;
    }

    @Override
    public List<Evaluate> myLike() {

        //查问题
        LambdaQueryWrapper<EvaluateLike> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(EvaluateLike::getLikeUserId, SecurityUtils.getCustomerLoginUserId());
        List<Long> replyIds = evaluateLikeMapper.selectList(likeWrapper).stream().map(EvaluateLike::getReplyId).collect(Collectors.toList());
        List<EvaluateReply> replyList=new ArrayList<>();
        if (replyIds.size() > 0) {
            LambdaQueryWrapper<EvaluateReply> replyWrapper = new LambdaQueryWrapper<>();
            replyWrapper.in(EvaluateReply::getId, replyIds);

            replyList = evaluateReplyMapper.selectList(replyWrapper);
        }
        Map<Long, List<EvaluateLike>> likeMap = evaluateLikeMapper.selectList(null).stream().collect(Collectors.groupingBy(EvaluateLike::getReplyId));
        Map<Long, List<Customer>> userMap = wechatMapper.selectList(null).stream().collect(Collectors.groupingBy(Customer::getId));
        Map<Long, List<Order>> orderMap = orderMapper.selectList(null).stream().collect(Collectors.groupingBy(Order::getCustomerId));

        List<Evaluate> evaluates = new ArrayList<>();
        for (EvaluateReply reply : replyList) {
            Evaluate one = getById(reply.getQuestionId());
            LambdaQueryWrapper<Product> productLambdaQueryWrapper = new LambdaQueryWrapper<>();
            productLambdaQueryWrapper.eq(Product::getId, one.getProductId());
            Product product = productMapper.selectOne(productLambdaQueryWrapper);
            //是否匿名
            if (reply.getIsAnonymous() == 1) {
                userMap.get(reply.getReplyUserId()).get(0).setNickname("匿名用户");
                reply.setReplyUserInfo(userMap.get(reply.getReplyUserId()).get(0));
            } else reply.setReplyUserInfo(userMap.get(reply.getReplyUserId()).get(0));
            //设置是否点赞回复
            LambdaQueryWrapper<EvaluateLike> likeWrapper1 = new LambdaQueryWrapper<>();
            likeWrapper1.eq(EvaluateLike::getLikeUserId, SecurityUtils.getCustomerLoginUserId());
            Map<Long, List<EvaluateLike>> myLikeMap = evaluateLikeMapper.selectList(likeWrapper).stream().collect(Collectors.groupingBy(EvaluateLike::getReplyId));
            //我是否点赞
            if (myLikeMap.get(reply.getId()) != null) reply.setIsLike(new Long(1));
            else reply.setIsLike(new Long(0));
            //设置回复数量
            Long likeNumber;
            likeNumber = likeMap.get(reply.getId()) == null ? new Long(0) : new Long(likeMap.get(reply.getId()).size());
            reply.setLikeNumber(likeNumber);
            //设置评论者购买信息
            reply.setProductName(product.getProductName());

            LambdaQueryWrapper<EvaluateCare> careWrapper = new LambdaQueryWrapper<>();
            careWrapper.eq(EvaluateCare::getCareUserId, SecurityUtils.getCustomerLoginUserId())
                    .eq(EvaluateCare::getQuestionId, reply.getQuestionId());
            Long careCount = evaluateCareMapper.selectCount(careWrapper);
            //是否匿名
            if (one.getIsAnonymous() == 1) {
                userMap.get(one.getUserId()).get(0).setNickname("匿名用户");
                one.setUserInfo(userMap.get(one.getUserId()).get(0));
            } else one.setUserInfo(userMap.get(one.getUserId()).get(0));
            //设置是否关注问题
            if (careCount > 0) one.setIsCare(new Long(1));
            else one.setIsCare(new Long(0));
            //设置回复数量
            LambdaQueryWrapper<EvaluateReply> replyWrapper1 = new LambdaQueryWrapper<>();
            replyWrapper1.eq(EvaluateReply::getQuestionId, reply.getQuestionId());
            one.setReplyNumber(evaluateReplyMapper.selectCount(replyWrapper1));
            //设置提问人信息
            one.setUserInfo(wechatMapper.selectById(SecurityUtils.getCustomerLoginUserId()));
            //当前人是否可以回复
            if (orderMap.get(SecurityUtils.getCustomerLoginUserId()) != null) {
                for (Order order : orderMap.get(SecurityUtils.getCustomerLoginUserId())) {
                    if (order.getId() == one.getProductId() && "completed".equals(order.getOrderStatus()))
                        one.setCanReply(new Long(0));
                    else one.setCanReply(new Long(1));
                }
            } else one.setCanReply(new Long(1));
            //设置首条回复
            one.setFirstReply(reply);

            evaluates.add(one);

        }


        return evaluates;
    }
}
