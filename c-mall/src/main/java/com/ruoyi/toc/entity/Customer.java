package com.ruoyi.toc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @ApiModelProperty(value = "用户ID")
    @Excel(name = "用户ID", sort = 1)
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    @Excel(name = "用户昵称", sort = 2)
    private String nickname;

    @ApiModelProperty(value = "真实姓名")
    @Excel(name = "姓名", sort = 3)
    private String name;

    @ApiModelProperty(value = "身份证号")
    @Excel(name = "身份证号", sort = 4)
    private String idCard;

    @ApiModelProperty(value = "手机号码")
    @Excel(name = "手机号", sort = 5)
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "用户注册时间")
    @Excel(name = "用户注册时间", width = 30, dateFormat = "yyyy-MM-dd", sort = 6)
    private Date registerDate;


    private String token;

    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;

    @ApiModelProperty(value = "性别(0=未知,1=男性,2=女性)")
    private Integer gender;

    @ApiModelProperty(value = "所在国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String region;

    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "插入时间")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;



    @ApiModelProperty(value = "角色身份(0=家属模式,1=关怀模式)")
    private String roleIdentity;



    @ApiModelProperty(value = "是否支付意向金(0=是,1=否)")
    private String isPay;

    @ApiModelProperty(value = "注册开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date startDate;

    @ApiModelProperty(value = "注册开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date endDate;

    @ApiModelProperty(value = "是否实名(0=是,1=否)")
    @TableField(exist = false)
    private String isRealName;

    @TableField(exist = false)
    private List<CusStakeholder> cusStakeholders;

    private String nodeStatus;

    private Long affiliateSalesId;

    private Long affiliateSalesManagerId;

    /**
     *  用户来源: 1-自主注册；2-销售推广
     */
    private Integer userSource;

    private String openId;

    /**
     * 省份label
     */
    private String provinceLabel;

    /**
     * 城市label
     */
    private String cityLabel;

    /**
     * 区域label
     */
    private String regionLabel;
}
