package com.fasteam.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasteam.dto.HighLevelQueryList;
import com.fasteam.dto.PaginationResult;
import com.fasteam.dto.RestResponse;
import com.fasteam.entity.Collection;
import com.fasteam.query.CollectionQuery;
import com.fasteam.service.CollectionService;
import com.fasteam.tools.FileUtil;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;
    @Autowired
    private FileUtil fileUtil;

    @RequestMapping(value = "/pagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse getWaresByType(@RequestBody CollectionQuery query) {
        return RestResponse.ok(collectionService.getByPagination(query.getName(), query.getPage(), query.getLimit()));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public RestResponse detail(@RequestParam("id") String id) {
        return RestResponse.ok(collectionService.getById(id));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RestResponse persist(@RequestBody Collection entity) throws Exception {
        collectionService.persist(entity);
        return RestResponse.ok();
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public RestResponse del(@RequestParam("id") String id) throws Exception {
        collectionService.delete(id);
        return RestResponse.ok();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RestResponse upload(HttpServletRequest request) throws IOException, MyException {
        List<JSONObject> result = new ArrayList<>();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> filesMap = multipartHttpServletRequest.getFileMap();
        String[] keys = multipartHttpServletRequest.getParameterMap().get("key");
        MultipartFile[] files = filesMap.values().toArray(new MultipartFile[]{});
        for (int i = 0; i < files.length; i++) {
            JSONObject object = new JSONObject();
            MultipartFile file = files[i];
            object.put("url", fileUtil.upload(file));
            object.put("id", keys[i]);
            result.add(object);
        }
        return RestResponse.ok(result);
    }

    //获取高级检索所需要的查询条件
    @RequestMapping(value = "high-level/column", method = RequestMethod.GET)
    public RestResponse highLevelColumn() throws Exception {
        return RestResponse.ok(Collection.getConditionData());
    }

    //高级检索，动态传入查询条件
    @RequestMapping(value = "high-level/query", method = RequestMethod.POST)
    public RestResponse highLevelQuery(@RequestBody HighLevelQueryList queryBean) throws Exception {
        return RestResponse.ok(collectionService.dynamicQuery(queryBean.getQueryBean(), queryBean.getPage(), queryBean.getLimit()));
    }
}
