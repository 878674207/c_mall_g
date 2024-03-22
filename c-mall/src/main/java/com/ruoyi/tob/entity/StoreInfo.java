package com.ruoyi.tob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("store_info")
@Accessors(chain = true)
public class StoreInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "店铺主管账号")
    @TableField(value = "user_name")
    private Long userName;

    @ApiModelProperty(value = "店铺名称")
    @TableField(value = "store_name")
    private String storeName;

    @ApiModelProperty(value = "店铺类型")
    @TableField(value = "store_type")
    private String storeType;

    @ApiModelProperty(value = "主营范围")
    @TableField(value = "main_business")
    private String mainBusiness;

    @ApiModelProperty(value = "店铺账户")
    @TableField(value = "store_account")
    private String storeAccount;

    @ApiModelProperty(value = "店铺状态")
    @TableField(value = "store_status")
    private String storeStatus;

    @ApiModelProperty(value = "店铺联系人")
    @TableField(value = "store_contact_person")
    private String storeContactPerson;

    @ApiModelProperty(value = "联系人电话")
    @TableField(value = "contact_phone")
    private String contactPhone;

    @ApiModelProperty(value = "联系人邮箱")
    @TableField(value = "contact_mail")
    private String contactMail;

    @ApiModelProperty(value = "实际办公地址")
    @TableField(value = "actual_office_address")
    private String actualOfficeAddress;

    @ApiModelProperty(value = "身份证正面")
    @TableField(value = "front_id")
    private String frontId;

    @ApiModelProperty(value = "身份证反面")
    @TableField(value = "back_id")
    private String backId;

    @ApiModelProperty(value = "结算方式")
    @TableField(value = "settlement_method")
    private String settlementMethod;

    @ApiModelProperty(value = "结算开户行")
    @TableField(value = "opening_bank")
    private String openingBank;

    @ApiModelProperty(value = "结算银行账号")
    @TableField(value = "settlement_bank_account")
    private String settlementBankAccount;

    @ApiModelProperty(value = "结算银行开户支行名称")
    @TableField(value = "name_settlement_bank")
    private String nameSettlementBank;

    @ApiModelProperty(value = "公司全称")
    @TableField(value = "ent_name")
    private String entName;

    @ApiModelProperty(value = "公司简介")
    @TableField(value = "ent_profile")
    private String entProfile;

    @ApiModelProperty(value = "入驻来源")
    @TableField(value = "source_entry")
    private String sourceEntry;

    @ApiModelProperty(value = "公司地址")
    @TableField(value = "ent_address")
    private String entAddress;

    @ApiModelProperty(value = "成立时间")
    @TableField(value = "establishment_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date establishmentTime;

    @ApiModelProperty(value = "企业注册号")
    @TableField(value = "ent_regist_number")
    private String entRegistNumber;

    @ApiModelProperty(value = "统一社会信用代码")
    @TableField(value = "social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty(value = "法人")
    @TableField(value = "legal_person")
    private String legalPerson;

    @ApiModelProperty(value = "法人身份证号")
    @TableField(value = "legal_person_id")
    private String legalPersonId;

    @ApiModelProperty(value = "营业执照经营范围")
    @TableField(value = "business_scope")
    private String businessScope;

    @ApiModelProperty(value = "注册资本")
    @TableField(value = "registered_capital")
    private String registeredCapital;

    @ApiModelProperty(value = "添加人")
    @TableField(value = "add_people")
    private String addPeople;

    @ApiModelProperty(value = "审批人")
    @TableField(value = "approved_by")
    private String approvedBy;

    @ApiModelProperty(value = "入驻时间")
    @TableField(value = "add_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date addTime;

    @ApiModelProperty(value = "查询条件入驻开始时间")
    @TableField(exist = false)
    private String startTime;
    @ApiModelProperty(value = "查询条件入驻结束时间")
    @TableField(exist = false)
    private String endTime;
    @ApiModelProperty(value = "相关资产证明")
    @TableField(exist = false)
    private List<StoreFile> attest;

    @TableField(exist = false)
    private String password;

    @ApiModelProperty(value = "logo图片")
    @TableField(value = "logo")
    private String logo;

    @ApiModelProperty(value = "禁用理由")
    @TableField(value = "forbid_reason")
    private String forbidReason;
}
