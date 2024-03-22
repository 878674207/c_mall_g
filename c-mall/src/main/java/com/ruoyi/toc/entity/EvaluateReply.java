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
@TableName("pms_evaluate_reply")
@Accessors(chain = true)
public class EvaluateReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "question_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("问题id")
    private Long questionId;

    @TableField(value = "reply_user_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("回复人id")
    private Long replyUserId;


    @TableField(value = "reply_content")
    @ApiModelProperty("回复内容")
    private String replyContent;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(value = "is_anonymous")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("是否匿名 0正常 1匿名")
    private Long isAnonymous;

    @TableField(exist = false)
    @ApiModelProperty("回复人信息")
    private Customer ReplyUserInfo;

    @TableField(exist = false)
    @ApiModelProperty("点赞数")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long likeNumber;

    @TableField(exist = false)
    @ApiModelProperty("当前查看人是否点赞 0未点赞 1已点赞")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long isLike;

    @TableField(exist = false)
    @ApiModelProperty("购买物品信息")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String productName;

}
