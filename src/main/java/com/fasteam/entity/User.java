package com.fasteam.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Description:  用户
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Data
@Entity
@Table(name = "sys_user")
public class User implements Serializable, UserDetails {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid2")
    private String id;
    private String username; //用户认证凭据
    private String password;
    private int enable; //是否可用 1可用，2不可用
    @Column(name = "role_code")
    private String role; //用户角色
    @Column(name = "create_time", length = 64)
    private String createTime;
    @Column(name = "real_name")
    private String realName;
    private Boolean checkCode; //true表示具有审批权限
    private String backup;


    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setCreateTime(formatter.format(date));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        //直接固定ROLE_ADMIN，不使用security的权限系统
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.enable == 1) {
            return true;
        } else {
            return false;
        }
    }

}
