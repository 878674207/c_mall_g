package com.ruoyi.toc.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.RedisConstants;
import com.ruoyi.common.core.domain.CommonException;
import com.ruoyi.common.core.domain.Result;
import com.ruoyi.common.core.domain.ResultStatusCode;
import com.ruoyi.common.core.domain.model.CustomerLoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.RegexUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;

import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.toc.entity.Customer;
import com.ruoyi.toc.mapper.CustomerMapper;
import com.ruoyi.toc.qo.CustomerLoginQo;
import com.ruoyi.toc.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName WechatServiceImpl
 * @Description TOD0
 * author axx
 * Date 2023/8/15 14:36
 * Version 1.0
 **/
@Service
@Slf4j
public class CustomerServiceImpl  implements CustomerService {


    @Value("${sms.codeTotal}")
    private String codeTotal;

    @Autowired
    private RedisCache redisCache;

    @Value("${token.secret}")
    private String tokenSecret;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TokenService tokenService;


    @Override
    public Result login(CustomerLoginQo customerLoginQo) throws CommonException {
        String phoneNumber = customerLoginQo.getPhone();
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new CommonException("手机号不存在");
        }
        if (RegexUtil.isPhoneInvalid(phoneNumber)) {
            return new Result<>(ResultStatusCode.PHONE_VERIFY_FAILED, null);
        }
        //2.从 redis 获取，检验验证码
        String cacheCode = redisCache.getCacheObject(RedisConstants.CUSTOMER_LOGIN_CODE_KEY + phoneNumber);
        String code = customerLoginQo.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            //3.不一致，报错
            return new Result<>(ResultStatusCode.CODE_VERIFY_FAILED, null);
        }


        // 生成令牌
        String uuid = IdUtils.fastSimpleUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.CUSTOM_LOGIN_USER_KEY, uuid);
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
        log.info("手机号:{}jwt生成的令牌是：{}", phoneNumber, token);

        Customer customer = customerMapper.selectOne(new QueryWrapper<Customer>().eq("phone", phoneNumber));
        if (Objects.isNull(customer)) {
            // 新用户创建

            Customer newCustomer  = new Customer();
            //使用雪花算法生成唯一id
            newCustomer.setGender(2);
            newCustomer.setPhone(phoneNumber);
            newCustomer.setNickname("微信用户");
            newCustomer.setName("微信用户");
            newCustomer.setCreatedAt(new Date());
            newCustomer.setUpdatedAt(new Date());
            newCustomer.setRegisterDate(new Date());
            newCustomer.setAvatarUrl(Constants.DEFAULT_AVATAR);
            customerMapper.insert(newCustomer);
            customer = newCustomer;
        }

        customer.setToken(token);


        // 往缓存里面塞入用户信息（UserDetails子类），用于接口鉴权
        tokenService.refreshCustomerToken(CustomerLoginUser.builder().id(customer.getId()).phone(phoneNumber)
                .token(uuid).build());


        return new Result<>(ResultStatusCode.OK, customer);
    }

    @Override
    public Object sendSms(String phone) {
        // 1.校验手机号
        if (RegexUtil.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return new Result<>(ResultStatusCode.PHONE_VERIFY_FAILED, "手机号格式错误！");
        }

        //查看缓存中是否存在code
        String cacheCode = redisCache.getCacheObject(RedisConstants.CUSTOMER_LOGIN_CODE_KEY + phone);
        if (StringUtils.isNotEmpty(cacheCode)) {
            return new Result<>(cacheCode);
        } else {
            String count = redisCache.getCacheObject(RedisConstants.SEND_SMS_COUNT + phone);
            Integer cacheCodeCount = Objects.isNull(count) ? 0 : Integer.valueOf(count);
            if (codeTotal.equals(cacheCodeCount)) {
                return new Result<>(ResultStatusCode.CODE_COUNT_LIMIT, null);
            }
            //生成随机验证码
            String code = RandomUtil.randomNumbers(6);
            log.info("手机号为：" + phone + "的验证码是" + code);

            Integer times = cacheCodeCount+ 1;
            redisCache.setCacheObject(RedisConstants.SEND_SMS_COUNT + phone, times.toString(), getEndTime(), TimeUnit.MILLISECONDS);
            //保存验证码到Redis
            redisCache.setCacheObject(RedisConstants.CUSTOMER_LOGIN_CODE_KEY + phone, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

            return new Result<>(code);
        }
    }

    @Override
    public Customer getUserInfo() {
        return customerMapper.selectById(SecurityUtils.getCustomerLoginUserId());
    }

    //获取当前时间到今天结束时间所剩余的毫秒数：
    public long getEndTime() {
        //获取当前时间的毫秒数
        long time = new java.util.Date().getTime();
        //获取到今天结束的毫秒数
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23); // Calendar.HOUR 12小时制。HOUR_OF_DAY 24小时制
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        long endTime = todayEnd.getTimeInMillis();
        //这里endTime-time获取的是到23：59：59：999的毫秒数。再加1才是到24点整的毫秒数
        return endTime - time + 1;
    }




}
