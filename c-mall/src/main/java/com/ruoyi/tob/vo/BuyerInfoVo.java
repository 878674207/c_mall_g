package com.ruoyi.tob.vo;

import lombok.Data;

@Data
public class BuyerInfoVo {
    private Long paymentAccount;

    private String receiverName;

    /** 收货人手机号码 */
    private String receiverPhone;

}
