package com.fasteam.service.intf;


import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;

import java.util.List;

/**
 * Description:  com.crow32.market.appservice.service.intf
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/2/20
 */
public interface InoutRecordServiceIntf {

    PaginationResult getByPagination(int page, int limit);

    PaginationResult getByWherePagination(String conditionType,String condition, String startTime, String endTime, int page, int limit);

    PaginationResult getReturnList(String condition,int page, int limit);
}
