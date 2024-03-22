package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("ALL")
public enum CustomerNodeStatusEnum {

    ACQUIRING_CUSTOMERS("acquiring_customers", "获客", 1),
    FOLLOW_UP("follow_up", "跟进", 2),
    RESERVATION("reservation", "预约", 3),
    TRADED("traded", "成交", 4);


    private String code;

    private String message;

    private int order;

    public static String getStatusByCode(String code) {
        for (CustomerNodeStatusEnum item : CustomerNodeStatusEnum.values()) {
            if (code.equals(item.code)) {
                return item.message;
            }
        }
        return Strings.EMPTY;
    }


    public static int getOrderyCode(String code) {
        for (CustomerNodeStatusEnum item : CustomerNodeStatusEnum.values()) {
            if (code.equals(item.code)) {
                return item.order;
            }
        }
        return 0;
    }

}
