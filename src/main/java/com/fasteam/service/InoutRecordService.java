package com.fasteam.service;

import com.fasteam.dao.InoutRecordDao;
import com.fasteam.dao.StoreDao;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Destroy;
import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;
import com.fasteam.service.intf.InoutRecordServiceIntf;
import com.fasteam.service.intf.StoreServiceIntf;
import com.fasteam.tools.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Description:  com.crow32.market.appservice.service
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
@Service
@Transactional
public class InoutRecordService implements InoutRecordServiceIntf {
    @Autowired
    private InoutRecordDao inoutRecordDao;

    @Override
    public PaginationResult getByPagination(int page, int limit) {
        int start = (page - 1) * limit;
        return PaginationResult.set(inoutRecordDao.getByPagnation(start, limit), Integer.valueOf(inoutRecordDao.count() + ""));
    }

    @Override
    public PaginationResult getByWherePagination(String conditionType, String condition, String startTime, String endTime, int page, int limit) {
        int start = (page - 1) * limit;
        int end = start + limit;
        if (StringUtils.isEmpty(startTime)) {
            startTime = "2000-01-01 00:00:00";
        }
        if (StringUtils.isEmpty(endTime)) {
            endTime = "3000-01-01 00:00:00";
        }
        if (StringUtils.isEmpty(conditionType)) {
            conditionType = null;
        }
        if (StringUtils.isEmpty(condition)) {
            condition = null;
        }
        return PaginationResult.set(inoutRecordDao.getByWherePagination(conditionType, condition, startTime, endTime, start, end), inoutRecordDao.countByWherePagination(conditionType, condition, startTime, endTime));
    }

    @Override
    public PaginationResult getReturnList(String condition, int page, int limit) {
        if (page > 0) {
            page = page - 1;
        }
        Page<InoutRecord> dataList = inoutRecordDao.getReturnList(condition, PageRequest.of(page, limit));
        return PaginationResult.set(dataList.getContent(), Integer.valueOf(dataList.getTotalElements() + ""));
    }
}
