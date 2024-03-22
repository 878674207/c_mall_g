package com.ruoyi.framework.aspectj;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.WechatLoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class GlobalLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalLogAspect.class);

    @Value("${auditlogs}")
    private Boolean auditlogsEable;

    @Pointcut("execution(public * com.*.*.*.controller.*.*(..))"
                + "|| execution(public * com.*.*.controller.*.*(..))")
    public void GlobalLogAspect() {
    }


    @Before("GlobalLogAspect()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        UserDetails user = SecurityUtils.getCurUser();
        if (auditlogsEable) {
            LOGGER.info(String.format("url  %s %s, %s", request.getServletPath(), request.getMethod(), getUserAccount(user)));
        }

    }

    @AfterReturning(value = "GlobalLogAspect()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }



//    @Around("GlobalLogAspect()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        //获取当前请求对象
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        Object result = joinPoint.proceed();
//
//        GlobalLogInfo globalLogInfo = new GlobalLogInfo();
//
//        // 请求url
//        String url = request.getRequestURL().toString();
//        globalLogInfo.setUrl(url);
//
//        // 接口域名
//        globalLogInfo.setBasePath(StrUtil.removeSuffix(url, URLUtil.url(url).getPath()));
//
//        // 请求ip
//        globalLogInfo.setIp(IpUtils.getIpAddr());
//
//        // 请求人
//        UserDetails user = SecurityUtils.getCurUser();
//        globalLogInfo.setUsername(getUserAccount(user));
//        // 请求方式
//        globalLogInfo.setMethod(request.getMethod());
//
//        // 请求的方法名
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        globalLogInfo.setMethodName(className + "." + methodName + "()");
//        if (!"initBinder".equals(methodName) && auditlogsEable) {
//            LOGGER.info("{}", JSONUtil.parse(globalLogInfo));
//        }
//        return result;
//    }

    private String getUserAccount(UserDetails user) {
        if (user instanceof LoginUser) {
            return  "pc user " + user.getUsername().toString();
        }

        if (user instanceof WechatLoginUser) {
            return  "cus user " + ((WechatLoginUser) user).getId().toString();
        }


        return Strings.EMPTY;
    }
}
