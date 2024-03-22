package com.ruoyi.framework.config;

import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.exception.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zxg
 * @date 2023/8/24 14:35
 * @Description 自定义拦截异常返回
 */
@ControllerAdvice
@ResponseBody
public class RequestErrorExceptionFilter {
    /**
     * 业务异常返回拦截并返回
     * @param customException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result returnException(CustomException customException){
        return Result.exceptionReturn(customException.getCustomResultErrorEnum());

    }
}
