package com.ruoyi.toc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.EvaluateReply;


public interface EvaluateReplyService {

    void saveReply(EvaluateReply evaluateReply);

    Page<EvaluateReply> index(EvaluateReply evaluateReply, BaseQo page);

    void del(Long id);

    void like(Long replyId);
    void dontLike(Long replyId);
}
