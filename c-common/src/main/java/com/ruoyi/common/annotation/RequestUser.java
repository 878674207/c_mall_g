package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * @author zxg
 * @date 2023/8/24 13:30
 * @Description 描述信息
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestUser {
    /**
     * 是否查询LoginUser对象所有信息，true则通过rpc接口查询
     */
    boolean value() default false;
}
