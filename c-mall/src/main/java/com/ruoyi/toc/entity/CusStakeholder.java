package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("cus_stakeholder")
public class CusStakeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Long wechatUserId;
    private String name;
    private String gender;

    @TableField(exist = false)
    private String genderText;
    private Integer age;
    private String relationShip;
    @TableField(exist = false)
    private String relationShipText;
    private String idCard;
    private String stakeholderPhone;
    private String physicalCondition;
    @TableField(exist = false)
    private String physicalConditionText;
    private String remark;
    private String createBy;

    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String areaCode;
    private String areaName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
