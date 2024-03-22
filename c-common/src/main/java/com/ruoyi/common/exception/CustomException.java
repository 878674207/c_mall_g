package com.ruoyi.common.exception;

import com.ruoyi.common.enums.CustomResultErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zxg
 * @date 2023/8/24 14:20
 * @Description 描述信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {
    /**
     * 枚举异常控制
     */
    private CustomResultErrorEnum customResultErrorEnum;

}
