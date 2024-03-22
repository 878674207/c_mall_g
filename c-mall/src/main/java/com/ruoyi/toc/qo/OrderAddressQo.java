package com.ruoyi.toc.qo;

import lombok.Data;

@Data
public class OrderAddressQo {
    private String receiverName;

    /** 收货人手机号码 */
    private String receiverPhone;

    private String address;

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
