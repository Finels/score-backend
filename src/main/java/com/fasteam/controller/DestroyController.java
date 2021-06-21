package com.fasteam.controller;

import com.fasteam.dto.PaginationResult;
import com.fasteam.dto.RestResponse;
import com.fasteam.entity.Accident;
import com.fasteam.entity.Destroy;
import com.fasteam.query.AccidentQuery;
import com.fasteam.query.DestroyQuery;
import com.fasteam.service.AccidentService;
import com.fasteam.service.DestroyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/destroy")
public class DestroyController {
    @Autowired
    private DestroyService destroyService;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getWaresByType(@RequestBody DestroyQuery query) {
        PaginationResult result = destroyService.getByPagination(query.getName(), query.getPage(), query.getLimit());
        return RestResponse.ok(result);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Destroy entity) throws Exception {
        destroyService.submitDestroyRequest(entity);
        return RestResponse.ok();
    }

}
