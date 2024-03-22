package com.ruoyi.tob.qo;

import com.ruoyi.common.core.qo.BaseQo;
import lombok.Data;

import java.util.List;

@Data
public class AfterSalesQo extends BaseQo {

    private List<Long> afterSalesIds;

    private String productName;

    private String orderNo;

    private String afterSalesNo;

    private String receiverInfo;

    private String afterSalesMethods;

    private Long id;

    private String afterSalesStatus;
}
