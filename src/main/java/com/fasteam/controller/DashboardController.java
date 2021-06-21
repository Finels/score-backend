package com.fasteam.controller;

import com.fasteam.dto.RestResponse;
import com.fasteam.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:  com.crow32.market.appservice.controller
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/21
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value = "/nums", method = RequestMethod.GET)
    public RestResponse getNums() {
        return RestResponse.ok(dashboardService.getNums());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResponse getList() throws Exception {
        return RestResponse.ok(dashboardService.getList());
    }

    @RequestMapping(value = "/tops", method = RequestMethod.GET)
    public RestResponse getTops() throws Exception {
        return RestResponse.ok(dashboardService.getTops());
    }

}
