package com.ruoyi.framework.config;

import com.ruoyi.common.annotation.RequestUser;
import com.ruoyi.common.core.domain.model.WechatLoginUser;
import com.ruoyi.common.enums.CustomResultErrorEnum;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxg
 * @date 2023/8/24 10:10
 * @Description 自定义注解控制器, 解析token获取用户信息
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private TokenService tokenService;

    /**
     * 执行判断是否满足使用规范
     *
     * @param methodParameter
     * @return true即跳出进行业务处理(resolveArgument)|| false不执行
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //是否是注解元素
        boolean isTrueRequestUser = methodParameter.hasParameterAnnotation(RequestUser.class);
        //是否注在指定用户信息对象
        boolean isAnnotationXwlUser = methodParameter.getParameterType().isAssignableFrom(WechatLoginUser.class);
        return (isTrueRequestUser && isAnnotationXwlUser);
    }

    /**
     * 解析token封装用户信息
     *
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        WechatLoginUser wechatUser;
        try {
            wechatUser = tokenService.getWechatLoginUser(request);
        } catch (Exception e) {
            throw new CustomException(CustomResultErrorEnum.FORBIDDEN);
        }
        //不排除以下情况->抛出无鉴权权限
        if (ObjectUtils.isEmpty(wechatUser)) throw new CustomException(CustomResultErrorEnum.FORBIDDEN);
        return wechatUser;
    }
}
