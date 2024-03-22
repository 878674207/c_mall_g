package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

/**
 *   机构审核状态枚举类
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("ALL")
public enum ApproveStatusEnum {
    PENDING_APPROVAL("pending_approval", "待审核"),
    APPROVED("approved", "审核通过"),
    REJECTED("rejected", "审核不通过");

    //套餐状态值
    private String code;
    //套餐状态
    private String status;

    public static String getStatusByCode(String code) {
        for (ApproveStatusEnum item : ApproveStatusEnum.values()) {
            if (code.equals(item.code)) {
                return item.status;
            }
        }
        return Strings.EMPTY;
    }
}