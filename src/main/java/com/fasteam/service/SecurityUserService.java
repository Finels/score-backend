package com.fasteam.service;

import com.fasteam.dao.UserDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Description:  用户登录相关service
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/21
 */
@Service
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDetail = userDao.findByUsernameAndEnable(username, 1);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", username));
        }
//        userDetail.setRole("ROLE_ADMIN");
        return userDetail;
    }

    public void register(User user) throws Exception {
        User user1 = userDao.findFirtByUsername(user.getUsername());
        if (!ObjectUtils.isEmpty(user1) && StringUtils.isEmpty(user.getId())) {
            throw new Exception("用户名已存在");
        }
        user.setEnable(1);
        if (StringUtils.isEmpty(user.getPassword())) {
            //如果传过来的密码是空的，则保留旧密码
            user.setPassword(user1.getPassword());
        }
        userDao.save(user);
    }

    public PaginationResult getNormalUserListPagination(int page, int limit) {
        int start = (page - 1) * limit;
        int total = 0;
        return PaginationResult.set(userDao.findByRole("NORMAL", start, limit), userDao.countByRole("NORMAL"));
    }

    public void del(String id) {
        userDao.updateEnableById(0, id);
    }
}
