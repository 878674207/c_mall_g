package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName CusInstitution
 * @Description 机构表
 * author axx
 * Date 2023/8/14 17:04
 * Version 1.0
 **/
@Data
@TableName("cus_image")
public class CusImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("file_name")
    private String fileName;

    @TableField("original_file_name")
    private String originalFileName;

    @TableField("url")
    private String fileUrl;

    @TableField(exist = false)
    private String previewUrl;

    @TableField("size")
    private String fileSize;

    @TableField("business_type")
    private String businessType;

    @TableField("business_id")
    private Long businessId;

    @TableField("sort_no")
    private Integer sortNo;

    @TableField("del_flag")
    private Integer delFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;
}
