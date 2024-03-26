package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName WxUserInfo
 * @Description TOD0
 * author axx
 * Date 2023/8/14 17:04
 * Version 1.0
 **/
@Data
@TableName("ums_customer")
@Accessors(chain = true)
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatarUrl;

    /** 性别  0-未知、1-男性、2-女性 */
    private Integer gender;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区县 */
    private String region;

    /** 手机号码 */
    private String phone;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 真实姓名 */
    private String name;

    /** 身份证号 */
    private String idCard;

    /** 注册日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    /** 0-未删除；1-已删除 */
    private String delFlag;

    /** 省份label */
    private String provinceLabel;

    /** 城市label */
    private String cityLabel;

    /** 区域label */
    private String regionLabel;

    @TableField(exist = false)
    private String token;
}
