package com.ruoyi.toc.qo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerLoginQo {
    @NotNull(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @NotNull(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;
}
