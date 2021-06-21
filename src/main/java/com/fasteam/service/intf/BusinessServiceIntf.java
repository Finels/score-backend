package com.fasteam.service.intf;


import com.fasteam.dto.BusinessCollection;
import com.fasteam.dto.InoutBusiness;
import com.fasteam.dto.MoveBusiness;
import com.fasteam.dto.PaginationResult;
import com.fasteam.entity.Business;
import com.fasteam.entity.Collection;
import com.fasteam.entity.InoutRecord;
import com.fasteam.entity.Store;
import com.fasteam.query.BusinessQuery;

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
public interface BusinessServiceIntf {

    PaginationResult getByPagination(BusinessQuery query);

    void handleMove(MoveBusiness business) throws Exception;

    void handIn(InoutBusiness business) throws Exception;

    void handOut(InoutBusiness business) throws Exception;

    void handReturn(InoutRecord record) throws Exception;

    Business getById(String id);

    List<Store> getCurrentStore(String cid, String name);

    List<BusinessCollection> getExportList(BusinessQuery query);

}
