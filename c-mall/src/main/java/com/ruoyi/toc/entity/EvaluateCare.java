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
@TableName("pms_evaluate_care")
@Accessors(chain = true)
public class EvaluateCare implements Serializable {

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

    @TableField(value = "care_user_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("关注人id")
    private Long careUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
