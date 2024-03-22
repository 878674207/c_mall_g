package com.ruoyi.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LocationRequest", description = "地理信息查询入参")
public class LocationRequest {
    @ApiModelProperty(value = "查询层级  1-省，2-市，3-县/区", required = true)
    private String level;

    private String code;

    private String parentCode;

}


