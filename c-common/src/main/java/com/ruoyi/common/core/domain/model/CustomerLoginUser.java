package com.ruoyi.common.core.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @ClassName userDto
 * @Description TOD0
 * author axx
 * Date 2023/8/23 11:05
 * Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerLoginUser implements UserDetails {
    private Long id;
    private String phone;

    private String createdAt;

    private String updatedAt;
    private String token;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录时间
     */
    private Long loginTime;

    private String uuid;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
