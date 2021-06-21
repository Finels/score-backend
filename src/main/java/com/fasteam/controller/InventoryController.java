package com.fasteam.controller;

import com.fasteam.dto.RestResponse;
import com.fasteam.entity.Inventory;
import com.fasteam.entity.Repair;
import com.fasteam.query.RepairQuery;
import com.fasteam.service.InventoryService;
import com.fasteam.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    public RestResponse pagination(@RequestBody RepairQuery query) {
        return RestResponse.ok(inventoryService.getByPagination(query.getName(), query.getPage(), query.getLimit()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Inventory entity) {
        inventoryService.persist(entity);
        return RestResponse.ok();
    }
}
