package com.ruoyi.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.WechatLoginUser;
import com.ruoyi.common.exception.base.BaseException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
@Slf4j
public class SecurityUtils {
    /**
     * 用户ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     **/
    public static Long getDeptId() {
        try {
            return getLoginUser().getDeptId();
        } catch (Exception e) {
            throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取店铺ID
     **/
    public static Long getStoreId() {
        try {
            return getLoginUser().getStoreId();
        } catch (Exception e) {
            throw new ServiceException("获取店铺ID异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户昵称
     **/
    public static String getNickName() {
        try {
            return getLoginUser().getUser().getNickName();
        } catch (Exception e) {
            throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser)getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户（捕获异常）
     **/
    public static LoginUser getLoginUserV2() {
        try {
            return (LoginUser)getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }


    public static UserDetails getCurUser() {
        LoginUser loginUser = getLoginUserV2();
        if (Objects.nonNull(loginUser)) {
            return loginUser;
        }
        WechatLoginUser wechatLoginUser = getCurWechatLoginUserV2();
        if (Objects.nonNull(wechatLoginUser)) {
            return wechatLoginUser;
        }
        return null;
    }

    /**
     * 获取当前微信用户
     **/
    public static Long getCurWechatLoginUserId() {
        try {
            return getCurWechatLoginUser().getId();
        } catch (Exception e) {
            throw new ServiceException("获取微信用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取当前微信用户
     **/
    public static WechatLoginUser getCurWechatLoginUser() {
        try {
            return (WechatLoginUser)getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取微信用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取当前微信用户（捕获异常）
     **/
    public static WechatLoginUser getCurWechatLoginUserV2() {
        try {
            return (WechatLoginUser)getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }










    /**
     * 获取角色
     **/
    public static List<SysRole> getLoginRole() {
        try {
            return getLoginUser().getUser().getRoles();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 判断当前用户是否是平台用户或者超级管理员
     */
    public static void getRole() {
        List<SysRole> roleList = SecurityUtils.getLoginRole();
        List<String> roleKeyList = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("platform-users");
        //判断两个list 是否 没有相同的元素
        if (roleKeyList.stream().noneMatch(list::contains)) {
            throw new BaseException("当前用户不是平台用户,不能删除");
        }
    }

    public static String getLoginRoleCode() {
        try {
            List<SysRole> roles = getLoginUser().getUser().getRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return Strings.EMPTY;
            }
            return roles.get(0).getRoleKey();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password
     *            密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword
     *            真实密码
     * @param encodedPassword
     *            加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId
     *            用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * sha256摘要加密, 建议可直接使用hutool包下的DigestUtil.sha256Hex方法
     *
     * @param strSrc
     *            原始字符串
     * @return 加密字符串
     */
    public static String encryptBySha256(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        String encName = "SHA-256";
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }

        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des += 0;
            }
            des += tmp;
        }

        return des;
    }
}
