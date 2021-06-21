package com.fasteam.controller;

import com.fasteam.dto.RestResponse;
import com.fasteam.entity.Repair;
import com.fasteam.query.RepairQuery;
import com.fasteam.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:  com.fasteam.controller
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/5/23
 */
@RestController
@RequestMapping("/repair")
public class RepairController {
    @Autowired
    private RepairService repairService;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    public RestResponse pagination(@RequestBody RepairQuery query) {
        return RestResponse.ok(repairService.getByPagination(query.getName(), query.getPage(), query.getLimit()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Repair entity) {
        repairService.persist(entity);
        return RestResponse.ok();
    }
}
