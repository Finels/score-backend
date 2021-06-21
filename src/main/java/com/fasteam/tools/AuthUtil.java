package com.fasteam.tools;

import com.fasteam.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Administrator on 2020/5/3.
 */
public class AuthUtil {
    public static User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
