//package com.ruoyi.framework.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
///**
// * @ClassName RequestMvcConfigutaionFilter
// * @Description TOD0
// * author axx
// * Date 2023/8/24 16:03
// * Version 1.0
// **/
//@Configuration
//@Component
//@SuppressWarnings("all")
//public class RequestMvcConfigutaionFilter  implements WebMvcConfigurer {
//    /**
//     * 自定义注解控制器集合体处理
//     * @param resolvers
//     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        //注入用户信息注解控制器
//        resolvers.add(new UserArgumentResolver());
//    }
//}
