package com.fasteam.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasteam.dto.PaginationResult;
import com.fasteam.dto.RestResponse;
import com.fasteam.dto.UserDto;
import com.fasteam.entity.JwtRequest;
import com.fasteam.entity.User;
import com.fasteam.service.SecurityUserService;
import com.fasteam.tools.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * Description:  该类的接口不会被权限拦截
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/21
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityUserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String jwtTokenHeader;

    @Value("${jwt.header}")
    private String authHeader;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestResponse login(@RequestBody JwtRequest request) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (DisabledException e) {
            throw new DisabledException("该账户已被锁定");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("用户名/密码错误");
        }
        final UserDetails userDetails = userService
                .loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return RestResponse.ok(new JSONObject() {{
            put("name", request.getUsername());
            put("token", jwtTokenHeader + token);
        }});
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestResponse register(@RequestBody UserDto userDto) throws Exception {
        User user = userDto.toBean();
        if (!StringUtils.isEmpty(user.getUsername())) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setCreateTime(new Date());
            userService.register(user);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResponse list(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return RestResponse.ok(userService.getNormalUserListPagination(page, limit));
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResponse del(@RequestParam("id") String id) {
        userService.del(id);
        return RestResponse.ok();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RestResponse logout(@RequestParam("token") String token) {
        if (!StringUtils.isEmpty(token)) {
            return RestResponse.ok();
        }
        return RestResponse.error();
    }

    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public RestResponse getInfo(HttpServletRequest request) {
        try {
            return RestResponse.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            return RestResponse.error();
        }
    }
}
