package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("pms_evaluate_like")
@Accessors(chain = true)
public class EvaluateLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;


    @TableField(value = "reply_id")
    @ApiModelProperty("回复id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long replyId;


    @TableField(value = "like_user_id")
    @ApiModelProperty("点赞人id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long likeUserId;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;



}
