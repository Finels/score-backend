package com.fasteam.controller;

import com.fasteam.dto.PaginationResult;
import com.fasteam.dto.RestResponse;
import com.fasteam.entity.InoutRecord;
import com.fasteam.query.InoutRecordQuery;
import com.fasteam.service.InoutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/record")
public class InoutRecordController {
    @Autowired
    private InoutRecordService inoutRecordService;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getWaresByType(@RequestBody InoutRecordQuery query) {
        PaginationResult result = null;
        if (StringUtils.isEmpty(query.getName()) && StringUtils.isEmpty(query.getStartTime()) && StringUtils.isEmpty(query.getEndTime()) && StringUtils.isEmpty(query.getType())) {
            result = inoutRecordService.getByPagination(query.getPage(), query.getLimit());
        } else {
            result = inoutRecordService.getByWherePagination(query.getType(), query.getName(), query.getStartTime(), query.getEndTime(), query.getPage(), query.getLimit());
        }

        return RestResponse.ok(result);
    }

    @RequestMapping(value = "/return/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getToReturn(@RequestBody InoutRecordQuery query) {
        PaginationResult result = inoutRecordService.getReturnList(query.getName(), query.getPage(), query.getLimit());
        return RestResponse.ok(result);
    }

}
