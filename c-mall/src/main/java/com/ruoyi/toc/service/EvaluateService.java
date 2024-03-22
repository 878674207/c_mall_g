package com.ruoyi.toc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.qo.BaseQo;
import com.ruoyi.toc.entity.Evaluate;


import java.util.List;

public interface EvaluateService {

    void saveQuestion(Evaluate evaluate);

    Page<Evaluate> index(Evaluate evaluate, BaseQo page);

    void del(Long id);


    void careQuestion(Long questionId);

    void dontCareQuestion(Long questionId);

    List<Evaluate> myCareQuestion();

    List<Evaluate> myReply();

    List<Evaluate> myQuestion();

    Evaluate selectByQuestionId(Long questionId);

    List<Evaluate> myLike();
}
