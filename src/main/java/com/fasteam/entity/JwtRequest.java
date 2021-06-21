package com.fasteam.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/2/22.
 */
@Data
public class JwtRequest implements Serializable {
    private String username;
    private String password;

}
