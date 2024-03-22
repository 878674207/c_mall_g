package com.ruoyi.tob.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class OrderOperationTime {
    private String orderNode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

}
