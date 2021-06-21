package com.fasteam.controller;

import com.fasteam.constant.BusinessConstant;
import com.fasteam.dto.InoutBusiness;
import com.fasteam.dto.MoveBusiness;
import com.fasteam.dto.RestResponse;
import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;
import com.fasteam.query.BusinessQuery;
import com.fasteam.query.CheckListQuery;
import com.fasteam.query.StoreQuery;
import com.fasteam.service.BusinessService;
import com.fasteam.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/store/pagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getStorePagination(@RequestBody StoreQuery query) {
        return RestResponse.ok(storeService.getByPagination(query.getCode(), query.getFloor(), query.getLocation(), query.getPage(), query.getLimit()));
    }

    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Store entity) throws Exception {
        storeService.persist(entity);
        return RestResponse.ok();
    }

    @RequestMapping(value = "/store", method = RequestMethod.DELETE)
    public RestResponse del(@RequestParam("id") String id) throws Exception {
        storeService.delete(id);
        return RestResponse.ok();
    }

    @RequestMapping(value = "/store/current", method = RequestMethod.GET)
    public RestResponse getCurrent(@RequestParam("cid") String cid, @RequestParam(value = "name", required = false) String name) {
        return RestResponse.ok(businessService.getCurrentStore(cid, name));
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    public RestResponse getBusinessPagination(@RequestBody BusinessQuery query) {
        return RestResponse.ok(businessService.getByPagination(query));
    }

    //入库
    @RequestMapping(value = "/in", method = RequestMethod.POST)
    public RestResponse inBusiness(@RequestBody InoutBusiness entity) throws Exception {
//        businessService.handIn(entity);
        businessService.submitInoutCheckRequest(entity, BusinessConstant.PUT_IN_STORE);
        return RestResponse.ok();
    }

    //出库
    @RequestMapping(value = "/out", method = RequestMethod.POST)
    public RestResponse outBusiness(@RequestBody InoutBusiness entity) throws Exception {
//        businessService.handOut(entity);
        businessService.submitInoutCheckRequest(entity, BusinessConstant.TAKE_OUT_STORE);
        return RestResponse.ok();
    }

    //移库
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public RestResponse moveBusiness(@RequestBody MoveBusiness entity) throws Exception {
//        businessService.handleMove(entity);
        businessService.submitMoveCheckRequest(entity);
        return RestResponse.ok();
    }

    //归库（出库后直接根据那条记录再做一次入库）
    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public RestResponse returnBusiness(@RequestBody InoutRecord record) throws Exception {
        businessService.handReturn(record);
        return RestResponse.ok();
    }

    //全部导出
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public RestResponse export(@RequestBody BusinessQuery query) throws Exception {
        query.setPage(1);
        query.setLimit(999999);
        return RestResponse.ok(businessService.getExportList(query));
    }

    //查询待审核列表
    @RequestMapping(value = "/uncheck/list", method = RequestMethod.POST)
    public RestResponse unchecked(@RequestBody CheckListQuery query) throws Exception {
        return RestResponse.ok(businessService.listChecked("not reviewed", query.getPage(), query.getLimit()));
    }

    //查询已审核的历史记录
    @RequestMapping(value = "/checked/list", method = RequestMethod.POST)
    public RestResponse checked(@RequestBody CheckListQuery query) throws Exception {
        return RestResponse.ok(businessService.listChecked("reviewed", query.getPage(), query.getLimit()));
    }

    //执行审核操作
    @RequestMapping(value = "/checked", method = RequestMethod.GET)
    public RestResponse doChecked(@RequestParam("id") String id) throws Exception {
        businessService.doCheck(id);
        return RestResponse.ok();
    }

    //执行审批拒绝操作
    @RequestMapping(value = "/checked", method = RequestMethod.DELETE)
    public RestResponse reject(@RequestParam("id") String id) throws Exception {
        businessService.doDel(id);
        return RestResponse.ok();
    }
}
