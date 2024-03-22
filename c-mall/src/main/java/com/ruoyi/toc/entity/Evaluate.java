package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("pms_evaluate")
@Accessors(chain = true)
public class Evaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "product_id")
    @ApiModelProperty("商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long productId;

    @TableField(value = "user_id")
    @ApiModelProperty("提问人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;


    @TableField(value = "question_content")
    @ApiModelProperty("问题内容")
    private String questionContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "is_anonymous")
    @ApiModelProperty("是否匿名 0正常 1匿名")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long isAnonymous;

    @TableField(exist = false)
    @ApiModelProperty("提问人信息")
    private WechatUser userInfo;

    @TableField(exist = false)
    @ApiModelProperty("首条回复")
    private EvaluateReply firstReply;

    @TableField(exist = false)
    @ApiModelProperty("当前查看人是否关注问题 0未关注 1已关注")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long isCare;

    @TableField(exist = false)
    @ApiModelProperty("回复条数")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long replyNumber;

    @TableField(exist = false)
    @ApiModelProperty("当前查看人是否可以回复 0:可以 1:不可以")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long canReply;

}
