package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("ALL")
public enum MealStatusEnum {
    OFF_SHELF(0, "下架"),
    ON_SHELF(1, "已上架"),
    PUBLISHED(2, "已发布");

    //套餐状态值
    private Integer code;
    //套餐状态
    private String status;

    public static String getStatusByCode(int code) {
        for (MealStatusEnum item : MealStatusEnum.values()) {
            if (item.getCode() == code) {
                return item.status;
            }
        }
        return Strings.EMPTY;
    }
}