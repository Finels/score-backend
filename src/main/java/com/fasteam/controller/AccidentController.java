package com.fasteam.controller;

import com.fasteam.dto.PaginationResult;
import com.fasteam.dto.RestResponse;
import com.fasteam.entity.Accident;
import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;
import com.fasteam.query.AccidentQuery;
import com.fasteam.query.InoutRecordQuery;
import com.fasteam.service.AccidentService;
import com.fasteam.service.InoutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/accident")
public class AccidentController {
    @Autowired
    private AccidentService accidentService;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getWaresByType(@RequestBody AccidentQuery query) {
        PaginationResult result = accidentService.getByPagination(query.getName(), query.getPage(), query.getLimit());

        return RestResponse.ok(result);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Accident entity) throws Exception {
        accidentService.persist(entity);
        return RestResponse.ok();
    }

}
