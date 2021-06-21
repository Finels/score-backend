package com.fasteam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:  com.crow32.market.appservice.dto
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/21
 */
@Data
@NoArgsConstructor
public class RestResponse {
    private boolean result;
    private String msg;
    private int code;
    private Object data;

    public static RestResponse unauthorized() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResult(false);
        restResponse.setCode(401);
        restResponse.setMsg("unauthorized");
        return restResponse;
    }

    public static RestResponse unauthorized(String msg) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResult(false);
        restResponse.setCode(401);
        restResponse.setMsg(msg);
        return restResponse;
    }

    public static RestResponse ok(Object data) {
        RestResponse restResponse = new RestResponse();
        restResponse.setResult(true);
        restResponse.setCode(200);
        restResponse.setData(data);
        return restResponse;
    }

    public static RestResponse ok() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResult(true);
        restResponse.setCode(200);
        return restResponse;
    }

    public static RestResponse error() {
        RestResponse restResponse = new RestResponse();
        restResponse.setResult(false);
        restResponse.setCode(500);
        return restResponse;
    }

    public static RestResponse error(String msg) {
        RestResponse restResponse = new RestResponse();
        restResponse.setMsg(msg);
        restResponse.setData(msg);
        restResponse.setResult(false);
        restResponse.setCode(500);
        return restResponse;
    }

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
