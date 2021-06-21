package com.fasteam.dto;

import com.fasteam.entity.User;
import lombok.Data;

import java.util.List;

/**
 * Description:  com.fasteam.dto
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/10/29
 */
@Data
public class UserDto {
    private String id;
    private String username; //用户认证凭据
    private String password;
    private int enable; //是否可用 1可用，2不可用
    private String role; //用户角色编码
    private String createTime;
    private String realName;
    private Boolean checkCode; //true表示具有审批权限
    private String backup;

    public User toBean() {
        User user = new User();
        if (this.id != null) {
            user.setId(this.id);
        }
        if (this.username != null) {
            user.setUsername(this.username);
        }
        if (this.password != null) {
            user.setPassword(this.password);
        }
        user.setEnable(this.enable);
        if (this.role != null) {
            user.setRole(this.role);
        }
        if (this.checkCode != null) {
            user.setCheckCode(this.checkCode);
        }
        if (this.createTime != null) {
            user.setCreateTime(this.createTime);
        }
        if (this.realName != null) {
            user.setRealName(this.realName);
        }
        if (this.backup != null) {
            user.setBackup(this.backup);
        }

        return user;
    }
}
